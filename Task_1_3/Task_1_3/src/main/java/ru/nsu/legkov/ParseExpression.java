package ru.nsu.legkov;

public class ParseExpression {
    public String str;
    int i = 0;


    public Expression parseExpression(String str) {
        this.str = str.trim();
        this.i = 0;
        Expression e = parseStart();

        skipSpaces();

        return e;
    }

    public void skipSpaces() {

    }

    public Expression parseStart() {
        return parse(0);
    }

    private Expression parse(int prior) {
        Expression l = pricesPrimary();


        StringBuilder var = new StringBuilder();
        StringBuilder num = new StringBuilder();

        boolean isVarNum = false; // true -> var, false -> num
        boolean fl = false;

        Expression e;

        while(true) {
            if (str.charAt(i1) == '+' || str.charAt(i1) == '-') {
                if (isVarNum) {
                    e = new Variable(var.toString());
                } else {
                    e = new Number(Integer.parseInt(num.toString()));
                }
                if (str.charAt(i1) == '+') {
                    return new Add(e, parse(i1, 2));
                } else {
                    return new Sub(e, parse(i1, 2));
                }
            }
            if (str.charAt(i1) == '*' || str.charAt(i1) == '/') {
                if (isVarNum) {
                    e = new Variable(var.toString());
                } else {
                    e = new Number(Integer.parseInt(num.toString()));
                }
                if (str.charAt(i1) == '*') {
                    return new Mul(e, parse(i1, 2));
                } else {
                    return new Div(e, parse(i1, 2));
                }
            }
            if (str.charAt(i1) == '(' || str.charAt(i1) == ')') {
                //возможно тут надо обновить i
                if(isVarNum) {
                    return new Variable(var.toString());
                } else {
                    return new Number(Integer.parseInt(num.toString()));
                }
            }
            if (fl) {
                if (isVarNum) {
                    var.append(str.charAt(i1));
                } else {
                    num.append(str.charAt(i1));
                }
                i1++;
            }
            else {
                fl = true;
                if(str.charAt(i1) >= '0' && str.charAt(i1) <= '9') {}
                else {
                    isVarNum = true;
                }

                if (isVarNum) {
                    var.append(str.charAt(i1));
                } else {
                    num.append(str.charAt(i1));
                }
                i1++;
            }
        }
    }

    private Expression parsePrimary() {
        skipSpaces();
        if (i >= str.length()) {
            throw new IllegalArgumentException("error i >= len");
        }
        char c = str.charAt(i);

        if (c == '(') {
            i++;
            Expression e = parse(0);
            skipSpaces();
            if(i >= str.length() || str.charAt(i) != ')') {
                throw new IllegalArgumentException("error not ') at pos " + i);
            }
            i++;
            return e;
        }
        if (c == '-') {
            i++;
            return new Sub(new Number(0), parsePrimary());
        }
        if (Character.isDigit(c)) {
            int start = i;
            while (i < str.length() && Character.isDigit(str.charAt(i))) {
                i++;
            }

            return new Number(Integer.parseInt(str.substring(start, i)));
        }

        if (Character.isLetter(c)) {
            int start = i;
            while (i < str.length() && Character.isLetter(str.charAt(i))) {
                i++;
            }

            return new Number(str.substring(start, i)1);
        }
    }



}


// 1+1+2*2