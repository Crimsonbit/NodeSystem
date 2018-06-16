const sampler_t smp = CLK_NORMALIZED_COORDS_FALSE | //Natural coordinates
                  CLK_ADDRESS_CLAMP | //Clamp to zeros
                  CLK_FILTER_NEAREST; //Don't interpolate
#define maskSize 1
__kernel void Erode(
		__read_only image2d_t sourceImage,
		__write_only image2d_t targetImage)
{
	
	int gidX = get_global_id(0);
	int gidY = get_global_id(1);
	int h = get_image_height(sourceImage);
	int w = get_image_width(sourceImage);
	int2 coord = (int2)(gidX, gidY);
	uint4 v_min = read_imageui(sourceImage, smp, coord);
	uint4 v_curr = v_min;
	for(int a = -maskSize; a < maskSize + 1; a++){
		for(int b = -maskSize; b < maskSize + 1; b++){
			int2 pos = coord + (int2)(a,b);
			if(pos.x >= 0 && pos.x < w && pos.y >= 0 && pos.y < h){
				v_curr = read_imageui(sourceImage, smp, pos);
			}
			v_min = min(v_min, v_curr);
		}
	}
	write_imageui(targetImage, coord, v_min);
}

__kernel void Dilate(
		__read_only image2d_t sourceImage,
		__write_only image2d_t targetImage)
{
	
	int gidX = get_global_id(0);
	int gidY = get_global_id(1);
	int h = get_image_height(sourceImage);
	int w = get_image_width(sourceImage);
	int2 coord = (int2)(gidX, gidY);
	uint4 v_max = read_imageui(sourceImage, smp, coord);
	uint4 v_curr = v_max;
	for(int a = -maskSize; a < maskSize + 1; a++){
		for(int b = -maskSize; b < maskSize + 1; b++){
			int2 pos = coord + (int2)(a,b);
			if(pos.x >= 0 && pos.x < w && pos.y >= 0 && pos.y < h){
				v_curr = read_imageui(sourceImage, smp, pos);
			}
			v_max = max(v_max, v_curr);
		}
	}
	write_imageui(targetImage, coord, v_max);
}

__kernel void Copy(__read_only image2d_t input, __write_only image2d_t output)
{
	int2 pos = {get_global_id(0), get_global_id(1)};
	uint4 val = read_imageui(input, smp, pos);
	write_imageui(output, pos, val);
}


