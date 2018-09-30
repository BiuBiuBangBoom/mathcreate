package po;

import java.io.File;
import java.io.IOException;

public class test {
	public static void main(String[] args) throws IOException {
		Expression.check(new File("Expression4.txt"), new File("answer4.txt"));
	}
}
