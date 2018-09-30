package po;

import java.io.File;
import java.io.IOException;

public class UserStart {
	public static void main(String[] args) {
		int range = -1, num = -1;
		boolean r_exist = false;
		for (int i = 0; i < args.length; i += 2) {
			switch (args[i]) {
			case "-r":
				if (Integer.parseInt(args[i + 1]) > 0) {
					r_exist = true;
					range = Integer.parseInt(args[i + 1]);
				}
				if (range > 10) {
					System.out.println("-r can't greater than 10");
					System.exit(0);
				}

				break;
			case "-n":
				num = Integer.parseInt(args[i + 1]);
				break;
			case "-e":
				String exerciseFileName = args[i + 1];
				String answerFileName = args[i + 3];
				File exerciseFile = new File(exerciseFileName);
				File answerFile = new File(answerFileName);
				try {
					Expression.check(exerciseFile, answerFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			default:
				break;
			}
		}
		if (!r_exist) {
			System.exit(0);
		} else {
			Expression ex = new Expression(range, num);
			File exfile  = new File("Expression.txt");
			File anfile = new File("answer.txt");
			ex.getExpression();
			try {
				ex.create(exfile, anfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
