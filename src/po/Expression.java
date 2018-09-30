package po;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *随机生成四则运算表达式
 *
 */
public class Expression {
    //表达式运算数的数值范围(0~range不包括range)
    private int range;
    //需要生成表达式的数目
    private int sum;
    //随机数生成器
    private Random random = new Random(System.currentTimeMillis());
    //使用hashSet来筛选出不重复的表达式
    private HashSet<String> hashSet = new HashSet<>();
    //字符串类型的表达式
    private String expression;

    public Expression(int range, int sum) {
        this.range = range;
        this.sum = sum;
    }

    /**
     * 取计算数
     * @return 返回0~range 的数值，不包括range
     */
    public Fraction getValue() {
        int denominator = random.nextInt(range - 1) + 1;
        int numerator = random.nextInt(denominator*denominator) - 1;
        return new Fraction(numerator, denominator);
    }

    /**
     *取计算符号
     * @return 返回+，-，*，/中任一个
     */
    public String getOperation() {
        String c = " ";
        int temp = random.nextInt(4);
        switch (temp) {
            case 0:
                c = "+";
                break;
            case 1:
                c = "-";
                break;
            case 2:
                c = "x";
                break;
            case 3:
                c = "÷";
                break;

        }
        return c;
    }
    /**
     * 比较两个分数的大小
     * @return true 大， false 小
     */
    private boolean isLarge(Fraction value1, Fraction value2) {
       if(value1.getNumerator()*value2.getDenominator() > value2.getNumerator()*value1.getDenominator()) {
           return true;
       } else
           return false;
    }

    /**
     * 第一种修改表达式方式
     * @param expression1
     */
    public String change1(String expression1, Fraction value, String operation) {
        Fraction value1 = new Fraction(1, 1);
        if(operation == "-") {
            value1 = value1.calculateStringExp(expression1);
            if(!isLarge(value1, value)) {
                if (value.getNumerator() != 0) {
                    operation = "÷";
                } else
                    operation = "+";
            }
        }
        if(operation == "÷") {
            value1 = value1.calculateStringExp(expression1);
            if(!isLarge(value1, value)) {
                operation = "-";
            }
            if(value.getNumerator() == 0) {
                operation = "x";
            }
        }
        return operation;
    }

    /**
     * 第二种修改表达式方式
     * @param expression1
     */
    private String change2(String expression1, Fraction value, String operation) {
        Fraction value1 = new Fraction(1, 1);
        value1 = value1.calculateStringExp(expression1);
        if (operation == "-") {
            if (!isLarge(value1, value)) {
                if (value.getNumerator() != 0) {
                    operation = "÷";
                } else {
                    operation = "+";
                }
            }
        }
            if (operation == "÷") {
                if (isLarge(value1, value)) {
                    operation = "-";
                } else if (value.getNumerator() == 0) {
                    operation = "x";
                }
            }
            return operation;
        }

        private String change3 (String expression, String operation1, Fraction value1, String operatin2, Fraction value2) {
            Fraction value = new Fraction(1, 1);
            value = value.calculateStringExp(expression);
            String expression1;
            if (operatin2 == "-" || operatin2 == "x" || operatin2 == "÷") {
                if (operation1 == "+") {
                    if (operatin2 == "-") {
                        expression1 = value + "+" + value1;
                        operatin2 = change1(expression1, value2, operatin2);
                        expression = "(" + expression + ")" + operation1 + value1 + operatin2 + value2;
                    }
                    if (operatin2 == "÷") {
                        if (isLarge(value1, value2)) {
                            value = value1;
                            value1 = value2;
                            value2 = value;
                        } else if(value2.getNumerator() == 0) {
                            operatin2 = "x";
                        }
                        expression = "(" + expression + ")" + operation1 + value1 + operatin2 + value2;
                    }
                }
                if (operation1 == "-") {
                    if (operatin2 == "-") {
                        operation1 = change1(expression, value1, operation1);
                        expression1 = value + operation1 + value1;
                        operatin2 = change1(expression1, value2, operatin2);
                        expression = "(" + expression + ")" + operation1 + value1 + operatin2 + value2;
                    }
                    if (operatin2 == "x" || operatin2 == "÷") {
                        Fraction value3 = new Fraction(1, 1);
                        if (operatin2 == "÷") {
                            if (isLarge(value1, value2)) {
                                value = value1;
                                value1 = value2;
                                value2 = value;
                            } else if(value2.getNumerator() == 0) {
                                operatin2 = "x";
                            }
                        }
                        value3 = value3.calculateStringExp(value1 + operatin2 + value2);
                        operation1 = change1(expression, value3, operation1);
                        expression = "(" + expression + ")" + operation1 + value1 + operatin2 + value2;
                    }
                }
                if (operation1 == "x") {
                    if (operatin2 == "-" || operatin2 == "÷") {
                        expression1 = value + operation1 + value1;
                        operatin2 = change1(expression1, value2, operatin2);
                        expression = "(" + expression + ")" + operation1 + value1 + operatin2 + value2;
                    }
                }
                if (operation1 == "÷") {
                    if (isLarge(value, value1)) {
                        operation1 = "-";
                        if (operatin2 == "x" || operatin2 == "÷") {
                            expression1 = value + operation1 + value1;
                            if (operatin2 == "÷") {
                                operatin2 = change1(expression1, value2, operatin2);
                            }
                            expression = "(" + "(" + expression + ")" + operation1 + value1 + ")" + operatin2 + value2;
                        }
                        expression = "(" + expression + ")" + operation1 + value1 + operatin2 + value2;
                    } else if(value1.getNumerator() != 0) {
                        operation1 = "x";
                        if(operatin2 == "-") {
                            operatin2 = "+";
                        }
                        expression = "(" + "(" + expression + ")" + operation1 + value1 + ")" + operatin2 + value2;
                    }
                }
            }
            return expression;
        }
        /**
         * 把表达式加载进hashset
         */
        public void getExpression () {
            int r = random.nextInt(3) + 1;
            while (hashSet.size() < sum) {
                switch (r) {
                    case 1:
                        hashSet.add(twoNumber());
                        break;
                    case 2:
                        hashSet.add(threeNumber());
                        break;
                    case 3:
                        hashSet.add(fourNumber());
                        break;

                }
            }
        }

        /**
         * 构造表达式的方式如a+b
         * @return String类型的表达式
         */
        private String firstModel () {
            Fraction value1 = getValue();
            Fraction value2 = getValue();
            String operation = getOperation();
            if (isLarge(value1, value2)) {
                if (operation.equals("÷")) {
                    operation = "-";
                }
            }
            if (!isLarge(value1, value2)) {
                if (operation.equals("-")) {
                    if(value2.getNumerator() != 0) {
                        operation = "÷";
                    } else {
                        operation = "+";
                    }
                }
            }

            expression = value1.toString() + operation + value2.toString();
            return expression;
        }


        /**
         * 两个操作数的表达式
         * @return String类型的表达式
         */
        private String twoNumber () {
            expression = firstModel();
            return expression;
        }
        /**
         * 三个操作数的表达式
         * @return String类型的表达式
         */
        private String threeNumber () {
            String expression1 = firstModel();
            String operation = getOperation();
            Fraction value = getValue();
            int flag = random.nextInt(3);
            switch (flag) {
                case 0:
                    expression = expression1 + "+" + value;
                    break;
                case 1:
                    operation = change1(expression1, value, operation);
                    expression = "(" + expression1 + ")" + operation + value;
                    break;
                case 2:
                    operation = change2(expression1, value, operation);
                    expression = value + operation + "(" + expression1 + ")";
            }
            return expression;
        }
        /**
         * 四个操作数的表达式
         * @return String类型的表达式
         */
        private String fourNumber () {
            String expression1 = firstModel();
            String expression2 = firstModel();

            String operation1 = getOperation();
            String operation2 = getOperation();
            Fraction value = getValue();
            Fraction value1 = getValue();
            int flag = random.nextInt(4);
            switch (flag) {
                case 0:
                    if (operation1.equals("-") || operation1.equals("÷")) {
                        Fraction value2 = new Fraction(1, 1);
                        value2 = value2.calculateStringExp(expression1);
                        if (operation1.equals("-")) {
                            if (!isLarge(value, value2)) {
                                if(value2.getNumerator() != 0) {
                                    operation1 = "÷";
                                } else {
                                    operation1 = "+";
                                }
                            }
                        } else {
                            if (isLarge(value, value2)) {
                                operation1 = "-";
                            }
                        }
                    }
                    expression = "(" + expression1 + ")" + operation1 + "(" + expression2 + ")";
                    break;
                case 1:
                    expression = change3(expression1, operation1, value, operation2, value1);
                    break;
                case 2:
                    if (operation1 == "-" || operation1 == "x" || operation1 == "÷") {
                        if (operation1 == "-") {
                            if (!isLarge(value, value.calculateStringExp(expression1))) {
                                if(value.calculateStringExp(expression1).getNumerator() != 0) {
                                    operation1 = "÷";
                                } else {
                                    operation1 = "+";
                                }
                                int i = random.nextInt(2);
                                if (i == 0) operation2 = "+";
                                if (i == 1) {
                                    operation2 = "-";
                                    Fraction value2 = new Fraction(1, 1);
                                    value2 = value2.calculateStringExp(value + operation1 + value.calculateStringExp(expression1));
                                    if (!isLarge(value2, value1)) {
                                        if(value1.getNumerator() != 0) {
                                            operation2 = "÷";
                                        } else {
                                            operation1 = "+";
                                        }
                                    }
                                }
                            }
                        }
                        if (operation1 == "÷") {
                            if (isLarge(value1, value.calculateStringExp(expression1))) {
                                operation1 = "-";
                                int i = random.nextInt(2);
                                if (i == 0) operation2 = "+";
                                if (i == 1) {
                                    operation2 = "-";
                                    Fraction value3 = new Fraction(1, 1);
                                    value3 = value3.calculateStringExp(value + operation1 + value.calculateStringExp(expression1));
                                    if (!isLarge(value3, value1)) {
                                        if(value1.getNumerator() != 0) {
                                            operation2 = "÷";
                                        } else {
                                            operation2 = "+";
                                        }

                                    }
                                }
                            }
                        }
                        if (operation1 == "x") {
                            value = value.calculateStringExp(value + operation1 + expression1);
                            if (operation2 == "-") {
                                if (!isLarge(value, value1)) {
                                    if(value1.getNumerator() != 0) {
                                        operation2 = "÷";
                                    } else {
                                        operation2 = "+";
                                    }
                                }
                            }
                            if (operation2 == "÷") {
                                if (isLarge(value, value1)) {
                                    operation2 = "-";
                                }
                                if(value1.getNumerator() != 0) {
                                    operation2 = "x";
                                }
                            }
                        }
                    }
                    expression = value + operation1 + "(" + expression1 + ")" + operation2 + value1;
                    break;
                case 3:
                    if (operation1 == "-") {
                        if (operation2 == "-") {
                            if (!isLarge(value, value1)) {
                                if(value1.getNumerator() != 0) {
                                    operation1 = "÷";
                                } else {
                                    operation1 = "+";
                                }
                                Fraction value4 = new Fraction(1, 1);
                                if (isLarge(value4.calculateStringExp(value + operation1 + value1), value.calculateStringExp(expression1))) {
                                    operation2 = "-";
                                }
                            }
                        }
                        if (operation2 == "x" || operation2 == "÷") {
                            if (operation2 == "÷") {
                                if (isLarge(value1, value.calculateStringExp(expression1))) {
                                    operation2 = "x";
                                }
                                if (!isLarge(value, value1.calculateStringExp(value1 + operation2 + value.calculateStringExp(expression1)))) {
                                    operation1 = "+";
                                }
                            }
                        }

                    }
                    if (operation1 == "÷") {
                        if (isLarge(value, value1)) {
                            operation1 = "x";
                            if (operation2 == "-") {
                                if (!isLarge(value.calculateStringExp(value + operation1 + value1), value.calculateStringExp(expression1))) {
                                    operation2 = "+";
                                }
                            }
                            if (operation2 == "÷") {
                                if (isLarge(value.calculateStringExp(value + operation1 + value1), value.calculateStringExp(expression1))) {
                                    operation2 = "-";
                                } else if(value.calculateStringExp(expression1).getNumerator() == 0) {
                                    operation2 = "x";
                                }
                            }
                        }
                    }
                    expression = value + operation1 + value1 + operation2 + "(" + expression1 + ")";
                    break;

            }
            return expression;
        }

        /**
         * 把表达式写到file文件中
         * @param file
         */
        public void create(File exfile, File anfile) throws IOException {
        	Iterator<String> lt = hashSet.iterator();
        	BufferedWriter ew = new BufferedWriter(new FileWriter(exfile));
        	BufferedWriter aw = new BufferedWriter(new FileWriter(anfile));
        	int count = 1;
        	while(lt.hasNext()) {
        		String s = String.valueOf(count) + "." + "   ";
        		String a = String.valueOf(count) + "." + "   ";
        		String temp = lt.next();
        		a = a + Fraction.calculateStringExp(temp).toString();
        		s = s + temp;
        		aw.write(a);
        		ew.write(s);
        		aw.newLine();
        		ew.newLine();
        		count++;
        	}
        }
        
        public static void check(File exfile, File anfile) throws IOException {
        	List<Integer> cor = new ArrayList<Integer>();
        	List<Integer> wor = new ArrayList<Integer>();
        	BufferedReader er = new BufferedReader(new FileReader(exfile));
        	BufferedReader ar = new BufferedReader(new FileReader(anfile));
        	String curerline = null;
        	String curarline = null;
        	while((curerline = er.readLine()) != null && (curarline = ar.readLine()) != null) {
        		int temp = curerline.indexOf(".");
        		String a = curerline;
        		int tihao = Integer.parseInt(a.substring(0, temp));
        		
        		String ertemp = curerline.substring(curerline.lastIndexOf(" ") + 1);
        		String artemp = curarline.substring(curarline.lastIndexOf(" ") + 1);
        		String coranswer = Fraction.calculateStringExp(ertemp).toString();
        		if(artemp.equals(coranswer)) 
        			cor.add(tihao);
        		else
        			wor.add(tihao);
        			
        	}
        	er.close();
        	ar.close();
        	File gradef = new File("grade.txt");
        	BufferedWriter gradew = new BufferedWriter(new FileWriter(gradef));
        	gradew.write("correct:" + cor.size());
        	gradew.newLine();
        	gradew.write(cor.toString());
        	gradew.newLine();
        	gradew.write("wrong:" + wor.size());
        	gradew.newLine();
        	if(!wor.isEmpty())
        		gradew.write(wor.toString());
        	gradew.newLine();
        	gradew.close();
        	System.out.println("批改完成");
        }
        
}