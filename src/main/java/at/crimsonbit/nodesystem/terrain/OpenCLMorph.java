package at.crimsonbit.nodesystem.terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_image_desc;
import org.jocl.cl_image_format;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

import at.crimsonbit.nodesystem.util.ImageUtils;

/**
 * Implementation of Morph using OpenGL
 * 
 * @author Alexander Daum
 *
 */
public class OpenCLMorph implements IMorph, AutoCloseable {
	private static cl_context context;
	private static cl_command_queue commandQueue;
	private static cl_program program;

	private static cl_kernel kernelErode;
	private static cl_kernel kernelDilate;
	private static cl_kernel kernelCopy;

	public OpenCLMorph() {
		initCL();
	}

	private static void initCL() {
		final int platformIndex = 0;
		final long deviceType = CL.CL_DEVICE_TYPE_ALL;
		final int deviceIndex = 0;

		// Enable exceptions and subsequently omit error checks in this sample
		CL.setExceptionsEnabled(true);

		// Obtain the number of platforms
		int numPlatformsArray[] = new int[1];
		CL.clGetPlatformIDs(0, null, numPlatformsArray);
		int numPlatforms = numPlatformsArray[0];

		// Obtain a platform ID
		cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
		CL.clGetPlatformIDs(platforms.length, platforms, null);
		cl_platform_id platform = platforms[platformIndex];

		// Initialize the context properties
		cl_context_properties contextProperties = new cl_context_properties();
		contextProperties.addProperty(CL.CL_CONTEXT_PLATFORM, platform);

		cl_device_id device = getCLDevice(deviceType, deviceIndex, platform);

		context = CL.clCreateContext(contextProperties, 1, new cl_device_id[] { device }, null, null, null);
		commandQueue = CL.clCreateCommandQueueWithProperties(context, device, null, null);

		String programSource;

		try (Reader reader = new FileReader(new File(OpenCLMorph.class.getResource("OpenCLMorph.cl").toURI()))) {
			StringBuffer sb = new StringBuffer();
			char[] buffer = new char[4096];
			while (reader.read(buffer) > 0) {
				sb.append(buffer);
			}
			programSource = sb.toString();
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}

		program = CL.clCreateProgramWithSource(context, 1, new String[] { programSource }, null, null);
		CL.clBuildProgram(program, 0, null, null, null, null);
	}

	private static cl_device_id getCLDevice(final long deviceType, final int deviceIndex, cl_platform_id platform) {
		// Obtain the number of devices for the platform
		int numDevicesArray[] = new int[1];
		CL.clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
		int numDevices = numDevicesArray[0];

		// Obtain a device ID
		cl_device_id devices[] = new cl_device_id[numDevices];
		CL.clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
		cl_device_id device = devices[deviceIndex];
		return device;
	}

	private static cl_kernel getKernelErode() {
		if (kernelErode == null) {
			kernelErode = CL.clCreateKernel(program, "Erode", null);
		}
		return kernelErode;
	}

	private static cl_kernel getKernelCopy() {
		if (kernelCopy == null) {
			kernelCopy = CL.clCreateKernel(program, "Copy", null);
		}
		return kernelCopy;
	}

	private static cl_kernel getKernelDilate() {
		if (kernelDilate == null) {
			kernelDilate = CL.clCreateKernel(program, "Dilate", null);
		}
		return kernelDilate;
	}

	private static void freeCLObjects() {
		if (kernelDilate != null)
			CL.clReleaseKernel(kernelDilate);
		if (kernelErode != null)
			CL.clReleaseKernel(kernelErode);
		if (kernelCopy != null) {
			CL.clReleaseKernel(kernelCopy);
		}
		CL.clReleaseProgram(program);
		CL.clReleaseContext(context);
		CL.clReleaseCommandQueue(commandQueue);

		kernelDilate = null;
		kernelErode = null;
		kernelCopy = null;
	}

	public BufferedImage Erode(BufferedImage input, int n) {
		BufferedImage out;
		out = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_RGB);
		cl_image_format imageFormat = createImageFormat();

		int[] inputData = ImageUtils.getData(input);
		int[] outputData = ImageUtils.getData(out);
		long global_work_size[] = new long[] { input.getWidth(), input.getHeight() };

		cl_image_desc imageType = createImageDescriptor(input);

		cl_mem input_image = CL.clCreateImage(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_USE_HOST_PTR, imageFormat,
				imageType, Pointer.to(inputData), null);

		cl_mem working_image = CL.clCreateImage(context, CL.CL_MEM_READ_WRITE, imageFormat, imageType, null, null);
		cl_mem output_image = CL.clCreateImage(context, CL.CL_MEM_READ_WRITE, imageFormat, imageType, null, null);

		CL.clSetKernelArg(getKernelCopy(), 0, Sizeof.cl_mem, Pointer.to(input_image));
		CL.clSetKernelArg(getKernelCopy(), 1, Sizeof.cl_mem, Pointer.to(working_image));
		CL.clEnqueueNDRangeKernel(commandQueue, getKernelCopy(), 2, null, global_work_size, null, 0, null, null);

		CL.clReleaseMemObject(input_image);

		CL.clSetKernelArg(getKernelErode(), 0, Sizeof.cl_mem, Pointer.to(working_image));
		CL.clSetKernelArg(getKernelErode(), 1, Sizeof.cl_mem, Pointer.to(output_image));

		CL.clSetKernelArg(getKernelCopy(), 0, Sizeof.cl_mem, Pointer.to(output_image));
		CL.clSetKernelArg(getKernelCopy(), 1, Sizeof.cl_mem, Pointer.to(working_image));

		for (int i = 0; i < n; i++) {
			CL.clEnqueueNDRangeKernel(commandQueue, getKernelErode(), 2, null, global_work_size, null, 0, null, null);
			CL.clEnqueueNDRangeKernel(commandQueue, getKernelCopy(), 2, null, global_work_size, null, 0, null, null);
		}
		CL.clEnqueueReadImage(commandQueue, output_image, true, new long[] { 0, 0, 0 },
				new long[] { input.getWidth(), input.getHeight(), 1 }, 0, 0, Pointer.to(outputData), 0, null, null);

		CL.clReleaseMemObject(working_image);
		CL.clReleaseMemObject(output_image);
		return out;
	}

	public BufferedImage Dilate(BufferedImage input, int n) {
		BufferedImage out;
		out = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_RGB);

		cl_image_format imageFormat = createImageFormat();
		cl_image_desc imageType = createImageDescriptor(input);
		int[] inputData = ImageUtils.getData(input);
		int[] outputData = ImageUtils.getData(out);
		long global_work_size[] = new long[] { input.getWidth(), input.getHeight() };

		cl_mem input_image = CL.clCreateImage(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_USE_HOST_PTR, imageFormat,
				imageType, Pointer.to(inputData), null);
		cl_mem working_image = CL.clCreateImage(context, CL.CL_MEM_READ_WRITE, imageFormat, imageType, null, null);
		cl_mem output_image = CL.clCreateImage(context, CL.CL_MEM_READ_WRITE, imageFormat, imageType, null, null);

		CL.clSetKernelArg(getKernelCopy(), 0, Sizeof.cl_mem, Pointer.to(input_image));
		CL.clSetKernelArg(getKernelCopy(), 1, Sizeof.cl_mem, Pointer.to(working_image));
		CL.clEnqueueNDRangeKernel(commandQueue, getKernelCopy(), 2, null, global_work_size, null, 0, null, null);

		CL.clSetKernelArg(getKernelCopy(), 0, Sizeof.cl_mem, Pointer.to(output_image));
		CL.clSetKernelArg(getKernelCopy(), 1, Sizeof.cl_mem, Pointer.to(working_image));

		CL.clReleaseMemObject(input_image);
		CL.clSetKernelArg(getKernelDilate(), 0, Sizeof.cl_mem, Pointer.to(working_image));
		CL.clSetKernelArg(getKernelDilate(), 1, Sizeof.cl_mem, Pointer.to(output_image));

		for (int i = 0; i < n; i++) {
			CL.clEnqueueNDRangeKernel(commandQueue, getKernelDilate(), 2, null, global_work_size, null, 0, null, null);
			CL.clEnqueueNDRangeKernel(commandQueue, getKernelCopy(), 2, null, global_work_size, null, 0, null, null);
		}
		CL.clEnqueueReadImage(commandQueue, output_image, true, new long[] { 0, 0, 0 },
				new long[] { input.getWidth(), input.getHeight(), 1 }, 0, 0, Pointer.to(outputData), 0, null, null);

		CL.clReleaseMemObject(working_image);
		CL.clReleaseMemObject(output_image);
		return out;
	}

	private cl_image_format createImageFormat() {
		cl_image_format imageFormat = new cl_image_format();
		imageFormat.image_channel_order = CL.CL_RGBA;
		imageFormat.image_channel_data_type = CL.CL_UNSIGNED_INT8;
		return imageFormat;
	}

	private cl_image_desc createImageDescriptor(BufferedImage input) {
		cl_image_desc imageType = new cl_image_desc();
		imageType.image_height = input.getHeight();
		imageType.image_width = input.getWidth();
		imageType.image_type = CL.CL_MEM_OBJECT_IMAGE2D;
		return imageType;
	}

	public void close() {
		freeCLObjects();
	}

}
