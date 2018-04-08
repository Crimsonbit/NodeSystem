package at.crimsonbit.nodesystem.util;
/**
 * 
 * @author NeonArtworks
 *
 */
public class RangeMapper {
	public static double mapValue(double value, double min_input, double max_input, int min_output, int max_output) {
		double inrange = max_input - min_input;
		/// (0.0))) value = 0.0; // Prevent DivByZero error
		value = (value - min_input) / inrange; // Map input range to [0.0 ... 1.0]
		if (value > max_output) {
			return max_output;
		}
		if (value < min_output) {
			return min_output;
		}
		return (min_output + (max_output - min_output) * value); // Map to output range and return result
	}
}
