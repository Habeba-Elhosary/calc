//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mariuszgromada.math.mxparser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.mariuszgromada.math.mxparser.mathcollection.MathFunctions;
import org.mariuszgromada.math.mxparser.mathcollection.NumberTheory;
import org.mariuszgromada.math.mxparser.mathcollection.ProbabilityDistributions;
import org.mariuszgromada.math.mxparser.mathcollection.SpecialFunctions;
import org.mariuszgromada.math.mxparser.mathcollection.Statistics;
import org.mariuszgromada.math.mxparser.parsertokens.KeyWord;
import org.mariuszgromada.math.mxparser.parsertokens.Token;
import org.mariuszgromada.math.mxparser.syntaxchecker.SyntaxChecker;

public class Expression extends PrimitiveElement {
    public static final int TYPE_ID = 100;
    public static final String TYPE_DESC = "User defined expression";
    static final int NOT_FOUND = -1;
    static final int FOUND = 0;
    static final boolean INTERNAL = true;
    private static final boolean WITH_EXP_STR = true;
    private static final boolean NO_EXP_STR = false;
    public static final boolean NO_SYNTAX_ERRORS = true;
    public static final boolean SYNTAX_ERROR_OR_STATUS_UNKNOWN = false;
    String expressionString;
    private String description;
    List<Argument> argumentsList;
    List<Function> functionsList;
    List<Constant> constantsList;
    private List<KeyWord> keyWordsList;
    private List<Token> initialTokens;
    private List<Token> tokensList;
    List<Expression> relatedExpressionsList;
    private double computingTime;
    private boolean expressionWasModified;
    boolean recursiveMode;
    private boolean verboseMode;
    boolean disableRounding;
    static final boolean DISABLE_ROUNDING = true;
    static final boolean KEEP_ROUNDING_SETTINGS = false;
    private boolean syntaxStatus;
    private String errorMessage;
    private boolean recursionCallPending;
    private int recursionCallsCounter;
    private boolean parserKeyWordsOnly;
    boolean UDFExpression = false;
    List<Double> UDFVariadicParamsAtRunTime;
    private boolean internalClone;
    private int optionsChangesetNumber = -1;
    private final String FUNCTION = "function";
    private final String ARGUMENT = "argument";
    private final String UNITCONST = "unit/const";
    private final String ERROR = "error";

    void addRelatedExpression(Expression var1) {
        if (var1 != null && var1 != this && !this.relatedExpressionsList.contains(var1)) {
            this.relatedExpressionsList.add(var1);
        }

    }

    void removeRelatedExpression(Expression var1) {
        this.relatedExpressionsList.remove(var1);
    }

    void showRelatedExpressions() {
        mXparser.consolePrintln();
        mXparser.consolePrintln(this.description + " = " + this.expressionString + ":");
        Iterator var1 = this.relatedExpressionsList.iterator();

        while(var1.hasNext()) {
            Expression var2 = (Expression)var1.next();
            mXparser.consolePrintln("-> " + var2.description + " = " + var2.expressionString);
        }

    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean getSyntaxStatus() {
        return this.syntaxStatus;
    }

    void setSyntaxStatus(boolean var1, String var2) {
        this.syntaxStatus = var1;
        this.errorMessage = var2;
        this.expressionWasModified = false;
    }

    void setExpressionModifiedFlag() {
        if (!this.recursionCallPending) {
            this.recursionCallPending = true;
            this.recursionCallsCounter = 0;
            this.internalClone = false;
            this.expressionWasModified = true;
            this.syntaxStatus = false;
            this.errorMessage = "Syntax status unknown.";
            Iterator var1 = this.relatedExpressionsList.iterator();

            while(var1.hasNext()) {
                Expression var2 = (Expression)var1.next();
                var2.setExpressionModifiedFlag();
            }

            this.recursionCallPending = false;
        }

    }

    private void expressionInternalVarsInit() {
        this.description = "";
        this.errorMessage = "";
        this.computingTime = 0.0D;
        this.recursionCallPending = false;
        this.recursionCallsCounter = 0;
        this.internalClone = false;
        this.parserKeyWordsOnly = false;
        this.disableRounding = false;
    }

    private void expressionInit() {
        this.argumentsList = new ArrayList();
        this.functionsList = new ArrayList();
        this.constantsList = new ArrayList();
        this.relatedExpressionsList = new ArrayList();
        this.setSilentMode();
        this.disableRecursiveMode();
        this.expressionInternalVarsInit();
    }

    public Expression(PrimitiveElement... var1) {
        super(100);
        this.expressionString = "";
        this.expressionInit();
        this.setExpressionModifiedFlag();
        this.addDefinitions(var1);
    }

    public Expression(String var1, PrimitiveElement... var2) {
        super(100);
        this.expressionInit();
        this.expressionString = new String(var1);
        this.setExpressionModifiedFlag();
        this.addDefinitions(var2);
    }

    Expression(String var1, boolean var2) {
        super(100);
        this.expressionInit();
        this.expressionString = new String(var1);
        this.setExpressionModifiedFlag();
        this.parserKeyWordsOnly = var2;
    }

    Expression(String var1, List<Token> var2, List<Argument> var3, List<Function> var4, List<Constant> var5, boolean var6, boolean var7, List<Double> var8) {
        super(100);
        this.expressionString = var1;
        this.initialTokens = var2;
        this.argumentsList = var3;
        this.functionsList = var4;
        this.constantsList = var5;
        this.relatedExpressionsList = new ArrayList();
        this.expressionWasModified = false;
        this.syntaxStatus = true;
        this.description = "_internal_";
        this.errorMessage = "";
        this.computingTime = 0.0D;
        this.recursionCallPending = false;
        this.recursionCallsCounter = 0;
        this.internalClone = false;
        this.parserKeyWordsOnly = false;
        this.UDFExpression = var7;
        this.UDFVariadicParamsAtRunTime = var8;
        this.disableRounding = var6;
        this.setSilentMode();
        this.disableRecursiveMode();
    }

    Expression(String var1, List<Argument> var2, List<Function> var3, List<Constant> var4, boolean var5, boolean var6, List<Double> var7) {
        super(100);
        this.expressionString = new String(var1);
        this.expressionInternalVarsInit();
        this.setSilentMode();
        this.disableRecursiveMode();
        this.argumentsList = var2;
        this.functionsList = var3;
        this.constantsList = var4;
        this.UDFExpression = var6;
        this.UDFVariadicParamsAtRunTime = var7;
        this.relatedExpressionsList = new ArrayList();
        this.setExpressionModifiedFlag();
    }

    private Expression(Expression var1) {
        super(100);
        this.expressionString = new String(var1.expressionString);
        this.description = new String(var1.description);
        this.argumentsList = var1.argumentsList;
        this.functionsList = var1.functionsList;
        this.constantsList = var1.constantsList;
        this.keyWordsList = var1.keyWordsList;
        this.relatedExpressionsList = var1.relatedExpressionsList;
        this.computingTime = 0.0D;
        this.expressionWasModified = var1.expressionWasModified;
        this.recursiveMode = var1.recursiveMode;
        this.verboseMode = var1.verboseMode;
        this.syntaxStatus = var1.syntaxStatus;
        this.errorMessage = new String(var1.errorMessage);
        this.recursionCallPending = var1.recursionCallPending;
        this.recursionCallsCounter = var1.recursionCallsCounter;
        this.parserKeyWordsOnly = var1.parserKeyWordsOnly;
        this.disableRounding = var1.disableRounding;
        this.UDFExpression = var1.UDFExpression;
        this.UDFVariadicParamsAtRunTime = var1.UDFVariadicParamsAtRunTime;
        this.internalClone = true;
    }

    public void setExpressionString(String var1) {
        this.expressionString = var1;
        this.setExpressionModifiedFlag();
    }

    public String getExpressionString() {
        return this.expressionString;
    }

    public void clearExpressionString() {
        this.expressionString = "";
        this.setExpressionModifiedFlag();
    }

    public void setDescription(String var1) {
        this.description = var1;
    }

    public String getDescription() {
        return this.description;
    }

    public void clearDescription() {
        this.description = "";
    }

    public void setVerboseMode() {
        this.verboseMode = true;
    }

    public void setSilentMode() {
        this.verboseMode = false;
    }

    public boolean getVerboseMode() {
        return this.verboseMode;
    }

    void setRecursiveMode() {
        this.recursiveMode = true;
    }

    void disableRecursiveMode() {
        this.recursiveMode = false;
    }

    public boolean getRecursiveMode() {
        return this.recursiveMode;
    }

    public double getComputingTime() {
        return this.computingTime;
    }

    public void addDefinitions(PrimitiveElement... var1) {
        PrimitiveElement[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            PrimitiveElement var5 = var2[var4];
            if (var5 != null) {
                int var6 = var5.getMyTypeId();
                if (var6 == 101) {
                    this.addArguments((Argument)var5);
                } else if (var6 == 104) {
                    this.addConstants((Constant)var5);
                } else if (var6 == 103) {
                    this.addFunctions((Function)var5);
                } else if (var6 == 102) {
                    this.addArguments((Argument)var5);
                }
            }
        }

    }

    public void removeDefinitions(PrimitiveElement... var1) {
        PrimitiveElement[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            PrimitiveElement var5 = var2[var4];
            if (var5 != null) {
                int var6 = var5.getMyTypeId();
                if (var6 == 101) {
                    this.removeArguments((Argument)var5);
                } else if (var6 == 104) {
                    this.removeConstants((Constant)var5);
                } else if (var6 == 103) {
                    this.removeFunctions((Function)var5);
                } else if (var6 == 102) {
                    this.removeArguments((Argument)var5);
                }
            }
        }

    }

    public void addArguments(Argument... var1) {
        Argument[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Argument var5 = var2[var4];
            if (var5 != null) {
                this.argumentsList.add(var5);
                if (var5.getArgumentBodyType() == 1) {
                    var5.addRelatedExpression(this);
                }
            }
        }

        this.setExpressionModifiedFlag();
    }

    public void defineArguments(String... var1) {
        String[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            Argument var6 = new Argument(var5, new PrimitiveElement[0]);
            var6.addRelatedExpression(this);
            this.argumentsList.add(var6);
        }

        this.setExpressionModifiedFlag();
    }

    public void defineArgument(String var1, double var2) {
        Argument var4 = new Argument(var1, var2);
        var4.addRelatedExpression(this);
        this.argumentsList.add(var4);
        this.setExpressionModifiedFlag();
    }

    public int getArgumentIndex(String var1) {
        int var2 = this.argumentsList.size();
        if (var2 <= 0) {
            return -1;
        } else {
            int var3 = 0;
            byte var4 = -1;

            while(var3 < var2 && var4 == -1) {
                if (((Argument)this.argumentsList.get(var3)).getArgumentName().equals(var1)) {
                    var4 = 0;
                } else {
                    ++var3;
                }
            }

            return var4 == 0 ? var3 : -1;
        }
    }

    public Argument getArgument(String var1) {
        int var2 = this.getArgumentIndex(var1);
        return var2 == -1 ? null : (Argument)this.argumentsList.get(var2);
    }

    public Argument getArgument(int var1) {
        return var1 >= 0 && var1 < this.argumentsList.size() ? (Argument)this.argumentsList.get(var1) : null;
    }

    public int getArgumentsNumber() {
        return this.argumentsList.size();
    }

    public void setArgumentValue(String var1, double var2) {
        int var4 = this.getArgumentIndex(var1);
        if (var4 != -1) {
            ((Argument)this.argumentsList.get(var4)).setArgumentValue(var2);
        }

    }

    public double getArgumentValue(String var1) {
        int var2 = this.getArgumentIndex(var1);
        return var2 != -1 ? ((Argument)this.argumentsList.get(var2)).getArgumentValue() : 0.0D / 0.0;
    }

    public void removeArguments(String... var1) {
        String[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            int var6 = this.getArgumentIndex(var5);
            if (var6 != -1) {
                Argument var7 = (Argument)this.argumentsList.get(var6);
                var7.removeRelatedExpression(this);
                this.argumentsList.remove(var6);
            }
        }

        this.setExpressionModifiedFlag();
    }

    public void removeArguments(Argument... var1) {
        Argument[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Argument var5 = var2[var4];
            if (var5 != null) {
                this.argumentsList.remove(var5);
                var5.removeRelatedExpression(this);
            }
        }

        this.setExpressionModifiedFlag();
    }

    public void removeAllArguments() {
        Iterator var1 = this.argumentsList.iterator();

        while(var1.hasNext()) {
            Argument var2 = (Argument)var1.next();
            var2.removeRelatedExpression(this);
        }

        this.argumentsList.clear();
        this.setExpressionModifiedFlag();
    }

    public void addConstants(Constant... var1) {
        Constant[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Constant var5 = var2[var4];
            if (var5 != null) {
                this.constantsList.add(var5);
                var5.addRelatedExpression(this);
            }
        }

        this.setExpressionModifiedFlag();
    }

    public void addConstants(List<Constant> var1) {
        this.constantsList.addAll(var1);
        Iterator var2 = var1.iterator();

        while(var2.hasNext()) {
            Constant var3 = (Constant)var2.next();
            var3.addRelatedExpression(this);
        }

        this.setExpressionModifiedFlag();
    }

    public void defineConstant(String var1, double var2) {
        Constant var4 = new Constant(var1, var2);
        var4.addRelatedExpression(this);
        this.constantsList.add(var4);
        this.setExpressionModifiedFlag();
    }

    public int getConstantIndex(String var1) {
        int var2 = this.constantsList.size();
        if (var2 <= 0) {
            return -1;
        } else {
            int var3 = 0;
            byte var4 = -1;

            while(var3 < var2 && var4 == -1) {
                if (((Constant)this.constantsList.get(var3)).getConstantName().equals(var1)) {
                    var4 = 0;
                } else {
                    ++var3;
                }
            }

            return var4 == 0 ? var3 : -1;
        }
    }

    public Constant getConstant(String var1) {
        int var2 = this.getConstantIndex(var1);
        return var2 == -1 ? null : (Constant)this.constantsList.get(var2);
    }

    public Constant getConstant(int var1) {
        return var1 >= 0 && var1 < this.constantsList.size() ? (Constant)this.constantsList.get(var1) : null;
    }

    public int getConstantsNumber() {
        return this.constantsList.size();
    }

    public void removeConstants(String... var1) {
        String[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            int var6 = this.getConstantIndex(var5);
            if (var6 != -1) {
                Constant var7 = (Constant)this.constantsList.get(var6);
                var7.removeRelatedExpression(this);
                this.constantsList.remove(var6);
            }
        }

        this.setExpressionModifiedFlag();
    }

    public void removeConstants(Constant... var1) {
        Constant[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Constant var5 = var2[var4];
            if (var5 != null) {
                this.constantsList.remove(var5);
                var5.removeRelatedExpression(this);
                this.setExpressionModifiedFlag();
            }
        }

    }

    public void removeAllConstants() {
        Iterator var1 = this.constantsList.iterator();

        while(var1.hasNext()) {
            Constant var2 = (Constant)var1.next();
            var2.removeRelatedExpression(this);
        }

        this.constantsList.clear();
        this.setExpressionModifiedFlag();
    }

    public void addFunctions(Function... var1) {
        Function[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Function var5 = var2[var4];
            if (var5 != null) {
                this.functionsList.add(var5);
                if (var5.getFunctionBodyType() == 1) {
                    var5.addRelatedExpression(this);
                }
            }
        }

        this.setExpressionModifiedFlag();
    }

    public void defineFunction(String var1, String var2, String... var3) {
        Function var4 = new Function(var1, var2, var3);
        this.functionsList.add(var4);
        var4.addRelatedExpression(this);
        this.setExpressionModifiedFlag();
    }

    public int getFunctionIndex(String var1) {
        int var2 = this.functionsList.size();
        if (var2 <= 0) {
            return -1;
        } else {
            int var3 = 0;
            byte var4 = -1;

            while(var3 < var2 && var4 == -1) {
                if (((Function)this.functionsList.get(var3)).getFunctionName().equals(var1)) {
                    var4 = 0;
                } else {
                    ++var3;
                }
            }

            return var4 == 0 ? var3 : -1;
        }
    }

    public Function getFunction(String var1) {
        int var2 = this.getFunctionIndex(var1);
        return var2 == -1 ? null : (Function)this.functionsList.get(var2);
    }

    public Function getFunction(int var1) {
        return var1 >= 0 && var1 < this.functionsList.size() ? (Function)this.functionsList.get(var1) : null;
    }

    public int getFunctionsNumber() {
        return this.functionsList.size();
    }

    public void removeFunctions(String... var1) {
        String[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            int var6 = this.getFunctionIndex(var5);
            if (var6 != -1) {
                Function var7 = (Function)this.functionsList.get(var6);
                var7.removeRelatedExpression(this);
                this.functionsList.remove(var7);
            }
        }

        this.setExpressionModifiedFlag();
    }

    public void removeFunctions(Function... var1) {
        Function[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Function var5 = var2[var4];
            if (var5 != null) {
                var5.removeRelatedExpression(this);
                this.functionsList.remove(var5);
            }
        }

        this.setExpressionModifiedFlag();
    }

    public void removeAllFunctions() {
        Iterator var1 = this.functionsList.iterator();

        while(var1.hasNext()) {
            Function var2 = (Function)var1.next();
            var2.removeRelatedExpression(this);
        }

        this.functionsList.clear();
        this.setExpressionModifiedFlag();
    }

    private void setToNumber(int var1, double var2, boolean var4) {
        Token var5 = (Token)this.tokensList.get(var1);
        if (mXparser.ulpRounding && !this.disableRounding) {
            if (var4) {
                if (!Double.isNaN(var2) && !Double.isInfinite(var2)) {
                    int var6 = MathFunctions.ulpDecimalDigitsBefore(var2);
                    if (var6 >= 0) {
                        var5.tokenValue = MathFunctions.round(var2, var6);
                    } else {
                        var5.tokenValue = var2;
                    }
                } else {
                    var5.tokenValue = var2;
                }
            } else {
                var5.tokenValue = var2;
            }
        } else {
            var5.tokenValue = var2;
        }

        var5.tokenTypeId = 0;
        var5.tokenId = 1;
        var5.keyWord = "_num_";
    }

    private void setToNumber(int var1, double var2) {
        this.setToNumber(var1, var2, false);
    }

    private void f1SetDecreaseRemove(int var1, double var2, boolean var4) {
        this.setToNumber(var1, var2, var4);
        --((Token)this.tokensList.get(var1)).tokenLevel;
        this.tokensList.remove(var1 + 1);
    }

    private void f1SetDecreaseRemove(int var1, double var2) {
        this.f1SetDecreaseRemove(var1, var2, false);
    }

    private void f2SetDecreaseRemove(int var1, double var2, boolean var4) {
        this.setToNumber(var1, var2, var4);
        --((Token)this.tokensList.get(var1)).tokenLevel;
        this.tokensList.remove(var1 + 2);
        this.tokensList.remove(var1 + 1);
    }

    private void f2SetDecreaseRemove(int var1, double var2) {
        this.f2SetDecreaseRemove(var1, var2, false);
    }

    private void f3SetDecreaseRemove(int var1, double var2, boolean var4) {
        this.setToNumber(var1, var2, var4);
        --((Token)this.tokensList.get(var1)).tokenLevel;
        this.tokensList.remove(var1 + 3);
        this.tokensList.remove(var1 + 2);
        this.tokensList.remove(var1 + 1);
    }

    private void f3SetDecreaseRemove(int var1, double var2) {
        this.f3SetDecreaseRemove(var1, var2, false);
    }

    private void opSetDecreaseRemove(int var1, double var2, boolean var4) {
        this.setToNumber(var1, var2, var4);
        this.tokensList.remove(var1 + 1);
        this.tokensList.remove(var1 - 1);
    }

    private void opSetDecreaseRemove(int var1, double var2) {
        this.opSetDecreaseRemove(var1, var2, false);
    }

    private void calcSetDecreaseRemove(int var1, double var2, boolean var4) {
        this.setToNumber(var1, var2, var4);
        --((Token)this.tokensList.get(var1)).tokenLevel;
        int var5 = var1 + 1;

        int var6;
        for(var6 = var5 + 1; ((Token)this.tokensList.get(var6)).tokenTypeId != 20 || ((Token)this.tokensList.get(var6)).tokenId != 2 || ((Token)this.tokensList.get(var6)).tokenLevel != ((Token)this.tokensList.get(var5)).tokenLevel; ++var6) {
        }

        for(int var7 = var6; var7 >= var5; --var7) {
            this.tokensList.remove(var7);
        }

    }

    private void calcSetDecreaseRemove(int var1, double var2) {
        this.calcSetDecreaseRemove(var1, var2, false);
    }

    private void variadicSetDecreaseRemove(int var1, double var2, int var4, boolean var5) {
        this.setToNumber(var1, var2, var5);
        --((Token)this.tokensList.get(var1)).tokenLevel;

        for(int var6 = var1 + var4; var6 > var1; --var6) {
            this.tokensList.remove(var6);
        }

    }

    private void variadicSetDecreaseRemove(int var1, double var2, int var4) {
        this.variadicSetDecreaseRemove(var1, var2, var4, false);
    }

    private void ifSetRemove(int var1, double var2, boolean var4) {
        int var5 = var1 + 1;
        int var6 = ((Token)this.tokensList.get(var5)).tokenLevel;

        int var7;
        for(var7 = var5 + 1; ((Token)this.tokensList.get(var7)).tokenTypeId != 20 || ((Token)this.tokensList.get(var7)).tokenId != 3 || ((Token)this.tokensList.get(var7)).tokenLevel != var6; ++var7) {
        }

        int var8;
        for(var8 = var7 + 1; ((Token)this.tokensList.get(var8)).tokenTypeId != 20 || ((Token)this.tokensList.get(var8)).tokenId != 3 || ((Token)this.tokensList.get(var8)).tokenLevel != var6; ++var8) {
        }

        int var9;
        for(var9 = var8 + 1; ((Token)this.tokensList.get(var9)).tokenTypeId != 20 || ((Token)this.tokensList.get(var9)).tokenId != 2 || ((Token)this.tokensList.get(var9)).tokenLevel != var6; ++var9) {
        }

        if (!Double.isNaN(var2)) {
            if (var2 != 0.0D) {
                this.setToNumber(var8 + 1, 0.0D / 0.0);
                ((Token)this.tokensList.get(var8 + 1)).tokenLevel = var6;
                this.removeTokens(var8 + 2, var9 - 1);
            } else {
                this.setToNumber(var7 + 1, 0.0D / 0.0);
                ((Token)this.tokensList.get(var7 + 1)).tokenLevel = var6;
                this.removeTokens(var7 + 2, var8 - 1);
            }
        } else {
            this.setToNumber(var7 + 1, 0.0D / 0.0);
            this.setToNumber(var8 + 1, 0.0D / 0.0);
            ((Token)this.tokensList.get(var7 + 1)).tokenLevel = var6;
            ((Token)this.tokensList.get(var8 + 1)).tokenLevel = var6;
            this.removeTokens(var8 + 2, var9 - 1);
            this.removeTokens(var7 + 2, var8 - 1);
        }

        this.setToNumber(var5 + 1, var2, var4);
        ((Token)this.tokensList.get(var5 + 1)).tokenLevel = var6;
        this.removeTokens(var5 + 2, var7 - 1);
        ((Token)this.tokensList.get(var1)).tokenId = 2;
    }

    private void removeTokens(int var1, int var2) {
        if (var1 < var2) {
            for(int var3 = var2; var3 >= var1; --var3) {
                this.tokensList.remove(var3);
            }
        } else if (var1 == var2) {
            this.tokensList.remove(var1);
        }

    }

    private void ifSetRemove(int var1, double var2) {
        this.ifSetRemove(var1, var2, false);
    }

    private List<Token> createInitialTokens(int var1, int var2, List<Token> var3) {
        ArrayList var4 = new ArrayList();

        for(int var6 = var1; var6 <= var2; ++var6) {
            Token var5 = ((Token)var3.get(var6)).clone();
            var4.add(var5);
        }

        return var4;
    }

    private int getParametersNumber(int var1) {
        int var2 = var1 + 1;
        if (var2 == this.initialTokens.size()) {
            return -1;
        } else if (((Token)this.initialTokens.get(var2)).tokenTypeId == 20 && ((Token)this.initialTokens.get(var2)).tokenId == 1) {
            int var3 = ((Token)this.initialTokens.get(var2)).tokenLevel;

            int var4;
            for(var4 = var2 + 1; ((Token)this.initialTokens.get(var4)).tokenTypeId != 20 || ((Token)this.initialTokens.get(var4)).tokenId != 2 || ((Token)this.initialTokens.get(var4)).tokenLevel != var3; ++var4) {
            }

            if (var4 == var2 + 1) {
                return 0;
            } else {
                int var5 = 0;

                for(int var6 = var2; var6 < var4; ++var6) {
                    Token var7 = (Token)this.initialTokens.get(var6);
                    if (var7.tokenTypeId == 20 && var7.tokenId == 3 && var7.tokenLevel == var3) {
                        ++var5;
                    }
                }

                return var5 + 1;
            }
        } else {
            return -1;
        }
    }

    private List<FunctionParameter> getFunctionParameters(int var1, List<Token> var2) {
        ArrayList var3 = new ArrayList();
        int var4 = var1 + 2;
        int var5 = ((Token)var2.get(var1 + 1)).tokenLevel;
        int var6 = var4;
        boolean var9 = false;
        ArrayList var10 = new ArrayList();
        String var11 = "";

        do {
            Token var12 = (Token)var2.get(var4);
            boolean var7 = false;
            boolean var8 = false;
            if (var12.tokenLevel == var5 && var12.tokenTypeId == 20) {
                if (var12.tokenId == 2) {
                    var8 = true;
                } else if (var12.tokenId == 3) {
                    var7 = true;
                }
            }

            if (!var8 && !var7) {
                var10.add(var12);
                var11 = var11 + var12.tokenStr;
            } else if (var4 > var1 + 2) {
                var3.add(new FunctionParameter(var10, var11, var6, var4 - 1));
                var10 = new ArrayList();
                var11 = "";
                var6 = var4 + 1;
            }

            if (var8) {
                var9 = true;
            } else {
                ++var4;
            }
        } while(!var9);

        return var3;
    }

    private ArgumentParameter getParamArgument(String var1) {
        ArgumentParameter var2 = new ArgumentParameter();
        var2.index = this.getArgumentIndex(var1);
        var2.argument = this.getArgument(var2.index);
        var2.presence = 0;
        if (var2.argument == null) {
            var2.argument = new Argument(var1, new PrimitiveElement[0]);
            this.argumentsList.add(var2.argument);
            var2.index = this.argumentsList.size() - 1;
            var2.presence = -1;
        } else {
            var2.initialValue = var2.argument.argumentValue;
            var2.initialType = var2.argument.argumentType;
            var2.argument.argumentValue = var2.argument.getArgumentValue();
            var2.argument.argumentType = 1;
        }

        return var2;
    }

    private void clearParamArgument(ArgumentParameter var1) {
        if (var1.presence == -1) {
            this.argumentsList.remove(var1.index);
        } else {
            var1.argument.argumentValue = var1.initialValue;
            var1.argument.argumentType = var1.initialType;
        }

    }

    private void FREE_ARGUMENT(int var1) {
        Argument var2 = (Argument)this.argumentsList.get(((Token)this.tokensList.get(var1)).tokenId);
        boolean var3 = var2.getVerboseMode();
        if (this.verboseMode) {
            var2.setVerboseMode();
        }

        this.setToNumber(var1, var2.getArgumentValue());
        if (!var3) {
            var2.setSilentMode();
        }

    }

    private void DEPENDENT_ARGUMENT(int var1) {
        Argument var2 = (Argument)this.argumentsList.get(((Token)this.tokensList.get(var1)).tokenId);
        boolean var3 = var2.getVerboseMode();
        if (this.verboseMode) {
            var2.setVerboseMode();
        }

        int var4 = this.tokensList.size();
        Token var5 = (Token)this.tokensList.get(var1);
        double var6 = var2.getArgumentValue();
        int var8 = this.tokensList.size();
        if (var4 == var8) {
            Token var9 = (Token)this.tokensList.get(var1);
            if (var5.tokenTypeId == var9.tokenTypeId && var5.tokenId == var9.tokenId) {
                this.setToNumber(var1, var6);
            }
        }

        if (!var3) {
            var2.setSilentMode();
        }

    }

    private void USER_FUNCTION(int var1) {
        Function var3 = (Function)this.functionsList.get(((Token)this.tokensList.get(var1)).tokenId);
        Function var2;
        if (var3.getRecursiveMode()) {
            var2 = var3.clone();
            var2.functionExpression.recursionCallsCounter = this.recursionCallsCounter;
        } else {
            var2 = var3;
        }

        var2.functionExpression.UDFVariadicParamsAtRunTime = this.getNumbers(var1);
        int var4 = var2.getParametersNumber();
        if (!var2.isVariadic) {
            for(int var5 = 0; var5 < var4; ++var5) {
                var2.setArgumentValue(var5, ((Token)this.tokensList.get(var1 + var5 + 1)).tokenValue);
            }
        }

        boolean var14 = var2.getVerboseMode();
        if (this.verboseMode) {
            var2.setVerboseMode();
        }

        int var6 = this.tokensList.size();
        Token var7 = (Token)this.tokensList.get(var1);

        double var8;
        try {
            var8 = var2.calculate();
        } catch (StackOverflowError var13) {
            var8 = 0.0D / 0.0;
            this.errorMessage = var13.getMessage();
        }

        int var10 = this.tokensList.size();
        if (var6 == var10) {
            Token var11 = (Token)this.tokensList.get(var1);
            if (var7.tokenTypeId == var11.tokenTypeId && var7.tokenId == var11.tokenId) {
                this.setToNumber(var1, var8);
                --((Token)this.tokensList.get(var1)).tokenLevel;

                for(int var12 = var4; var12 > 0; --var12) {
                    this.tokensList.remove(var1 + var12);
                }
            }
        }

        if (!var14) {
            var2.setSilentMode();
        }

    }

    private void USER_CONSTANT(int var1) {
        Constant var2 = (Constant)this.constantsList.get(((Token)this.tokensList.get(var1)).tokenId);
        this.setToNumber(var1, var2.getConstantValue());
    }

    private void RECURSIVE_ARGUMENT(int var1) {
        double var2 = ((Token)this.tokensList.get(var1 + 1)).tokenValue;
        RecursiveArgument var4 = (RecursiveArgument)this.argumentsList.get(((Token)this.tokensList.get(var1)).tokenId);
        boolean var5 = var4.getVerboseMode();
        if (this.verboseMode) {
            var4.setVerboseMode();
        }

        double var6 = var4.getArgumentValue(var2);
        this.f1SetDecreaseRemove(var1, var6);
        if (!var5) {
            var4.setSilentMode();
        }

    }

    private void CONSTANT(int var1) {
        double var2 = 0.0D / 0.0;
        switch(((Token)this.tokensList.get(var1)).tokenId) {
            case 1:
                var2 = 3.141592653589793D;
                break;
            case 2:
                var2 = 2.718281828459045D;
                break;
            case 3:
                var2 = 0.5772156649015329D;
                break;
            case 4:
                var2 = 1.618033988749895D;
                break;
            case 5:
                var2 = 1.324717957244746D;
                break;
            case 6:
                var2 = 0.70258D;
                break;
            case 7:
                var2 = 4.66920160910299D;
                break;
            case 8:
                var2 = 2.5029078750958926D;
                break;
            case 9:
                var2 = 0.6601618158468696D;
                break;
            case 10:
                var2 = 0.26149721284764277D;
                break;
            case 11:
                var2 = 1.9021605823D;
                break;
            case 12:
                var2 = 0.87058838D;
                break;
            case 13:
                var2 = -2.7E-9D;
                break;
            case 14:
                var2 = 0.915965594177219D;
                break;
            case 15:
                var2 = 0.7642236535892206D;
                break;
            case 16:
                var2 = 1.13198824D;
                break;
            case 17:
                var2 = 1.0D;
                break;
            case 18:
                var2 = 1.451369234883381D;
                break;
            case 19:
                var2 = 1.6066951524152917D;
                break;
            case 20:
                var2 = 0.2801694990238691D;
                break;
            case 21:
                var2 = 0.30366300289873266D;
                break;
            case 22:
                var2 = 0.353236371854996D;
                break;
            case 23:
                var2 = 0.6243299885435508D;
                break;
            case 24:
                var2 = 0.6434105463D;
                break;
            case 25:
                var2 = 0.6627434193491816D;
                break;
            case 26:
                var2 = 0.8093940205D;
                break;
            case 27:
                var2 = 1.0986858055D;
                break;
            case 28:
                var2 = 3.2758229187218113D;
                break;
            case 29:
                var2 = 1.2020569031595942D;
                break;
            case 30:
                var2 = 1.3063778838630806D;
                break;
            case 31:
                var2 = 1.4560749485826896D;
                break;
            case 32:
                var2 = 1.4670780794D;
                break;
            case 33:
                var2 = 1.5396007178D;
                break;
            case 34:
                var2 = 1.7052111401053678D;
                break;
            case 35:
                var2 = 2.5849817595792532D;
                break;
            case 36:
                var2 = 2.6854520010653062D;
                break;
            case 37:
                var2 = 2.8077702420285195D;
                break;
            case 38:
                var2 = 0.5D;
                break;
            case 39:
                var2 = 2.295587149392638D;
                break;
            case 40:
                var2 = 0.5671432904097838D;
                break;
            case 41:
                var2 = 0.187859D;
                break;
            case 42:
                var2 = 1.045163780117493D;
                break;
            case 43:
                var2 = 0.5963473623231941D;
                break;
            case 101:
                var2 = 2.99792458E8D;
                break;
            case 102:
                var2 = 6.67408E-11D;
                break;
            case 103:
                var2 = 9.80665D;
                break;
            case 104:
                var2 = 6.62607004E-34D;
                break;
            case 105:
                var2 = 1.0545718001391127E-34D;
                break;
            case 106:
                var2 = 1.616229E-35D;
                break;
            case 107:
                var2 = 2.17647E-8D;
                break;
            case 108:
                var2 = 5.39116E-44D;
                break;
            case 201:
                var2 = 9.4607304725808E15D;
                break;
            case 202:
                var2 = 1.495978707E11D;
                break;
            case 203:
                var2 = 3.085677581491362E16D;
                break;
            case 204:
                var2 = 3.085677581491362E19D;
                break;
            case 205:
                var2 = 6378137.0D;
                break;
            case 206:
                var2 = 6356752.3D;
                break;
            case 207:
                var2 = 6371008.8D;
                break;
            case 208:
                var2 = 5.9722E24D;
                break;
            case 209:
                var2 = 1.495980229906324E11D;
                break;
            case 210:
                var2 = 1737100.0D;
                break;
            case 211:
                var2 = 7.34582809714E22D;
                break;
            case 212:
                var2 = 3.84399E8D;
                break;
            case 213:
                var2 = 6.957E8D;
                break;
            case 214:
                var2 = 1.98842039204614E30D;
                break;
            case 215:
                var2 = 2439700.0D;
                break;
            case 216:
                var2 = 3.3026266E23D;
                break;
            case 217:
                var2 = 5.79090365522286E10D;
                break;
            case 218:
                var2 = 6051800.0D;
                break;
            case 219:
                var2 = 4.867343E24D;
                break;
            case 220:
                var2 = 1.082089270091724E11D;
                break;
            case 221:
                var2 = 3389500.0D;
                break;
            case 222:
                var2 = 6.390254E23D;
                break;
            case 223:
                var2 = 2.279391340303053E11D;
                break;
            case 224:
                var2 = 6.9911E7D;
                break;
            case 225:
                var2 = 1.8979651600000002E27D;
                break;
            case 226:
                var2 = 7.782978821038201E11D;
                break;
            case 227:
                var2 = 5.8232E7D;
                break;
            case 228:
                var2 = 5.6830857980000005E26D;
                break;
            case 229:
                var2 = 1.42939269475143E12D;
                break;
            case 230:
                var2 = 2.5362E7D;
                break;
            case 231:
                var2 = 8.681189920000001E25D;
                break;
            case 232:
                var2 = 2.87503171826088E12D;
                break;
            case 233:
                var2 = 2.4622E7D;
                break;
            case 234:
                var2 = 1.024053134E26D;
                break;
            case 235:
                var2 = 4.504449781152961E12D;
                break;
            case 301:
                var2 = 1.0D;
                break;
            case 302:
                var2 = 0.0D;
                break;
            case 303:
                var2 = (double)this.UDFVariadicParamsAtRunTime.size();
                break;
            case 999:
                var2 = 0.0D / 0.0;
        }

        this.setToNumber(var1, var2);
    }

    private void UNIT(int var1) {
        double var2 = 0.0D / 0.0;
        switch(((Token)this.tokensList.get(var1)).tokenId) {
            case 1:
                var2 = 0.01D;
                break;
            case 2:
                var2 = 0.001D;
                break;
            case 101:
                var2 = 1.0E24D;
                break;
            case 102:
                var2 = 1.0E21D;
                break;
            case 103:
                var2 = 1.0E18D;
                break;
            case 104:
                var2 = 1.0E15D;
                break;
            case 105:
                var2 = 1.0E12D;
                break;
            case 106:
                var2 = 1.0E9D;
                break;
            case 107:
                var2 = 1000000.0D;
                break;
            case 108:
                var2 = 1000.0D;
                break;
            case 109:
                var2 = 100.0D;
                break;
            case 110:
                var2 = 10.0D;
                break;
            case 111:
                var2 = 0.1D;
                break;
            case 112:
                var2 = 0.01D;
                break;
            case 113:
                var2 = 0.001D;
                break;
            case 114:
                var2 = 1.0E-6D;
                break;
            case 115:
                var2 = 1.0E-9D;
                break;
            case 116:
                var2 = 1.0E-12D;
                break;
            case 117:
                var2 = 1.0E-15D;
                break;
            case 118:
                var2 = 1.0E-18D;
                break;
            case 119:
                var2 = 1.0E-21D;
                break;
            case 120:
                var2 = 1.0E-24D;
                break;
            case 201:
                var2 = 1.0D;
                break;
            case 202:
                var2 = 1000.0D;
                break;
            case 203:
                var2 = 0.01D;
                break;
            case 204:
                var2 = 0.001D;
                break;
            case 205:
                var2 = 0.025400000000000002D;
                break;
            case 206:
                var2 = 0.9144D;
                break;
            case 207:
                var2 = 0.3048D;
                break;
            case 208:
                var2 = 1609.344D;
                break;
            case 209:
                var2 = 1852.0D;
                break;
            case 301:
                var2 = 1.0D;
                break;
            case 302:
                var2 = 1.0E-4D;
                break;
            case 303:
                var2 = 1.0E-6D;
                break;
            case 304:
                var2 = 100.0D;
                break;
            case 305:
                var2 = 10000.0D;
                break;
            case 306:
                var2 = 4046.8564224000006D;
                break;
            case 307:
                var2 = 1000000.0D;
                break;
            case 401:
                var2 = 1.0E-9D;
                break;
            case 402:
                var2 = 1.0000000000000002E-6D;
                break;
            case 403:
                var2 = 1.0D;
                break;
            case 404:
                var2 = 1.0E9D;
                break;
            case 405:
                var2 = 1.0000000000000002E-6D;
                break;
            case 406:
                var2 = 0.0010000000000000002D;
                break;
            case 407:
                var2 = 0.003785411780000001D;
                break;
            case 408:
                var2 = 4.7317647300000007E-4D;
                break;
            case 501:
                var2 = 1.0D;
                break;
            case 502:
                var2 = 0.001D;
                break;
            case 503:
                var2 = 60.0D;
                break;
            case 504:
                var2 = 3600.0D;
                break;
            case 505:
                var2 = 86400.0D;
                break;
            case 506:
                var2 = 604800.0D;
                break;
            case 507:
                var2 = 3.15576E7D;
                break;
            case 508:
                var2 = 1.0D;
                break;
            case 509:
                var2 = 0.001D;
                break;
            case 510:
                var2 = 1.0E-6D;
                break;
            case 511:
                var2 = 0.01D;
                break;
            case 512:
                var2 = 1000.0D;
                break;
            case 513:
                var2 = 0.0283495231D;
                break;
            case 514:
                var2 = 0.45359237D;
                break;
            case 601:
                var2 = 1.0D;
                break;
            case 602:
                var2 = 1024.0D;
                break;
            case 603:
                var2 = 1048576.0D;
                break;
            case 604:
                var2 = 1.073741824E9D;
                break;
            case 605:
                var2 = 1.099511627776E12D;
                break;
            case 606:
                var2 = 1.125899906842624E15D;
                break;
            case 607:
                var2 = 1.15292150460684698E18D;
                break;
            case 608:
                var2 = 1.1805916207174113E21D;
                break;
            case 609:
                var2 = 1.2089258196146292E24D;
                break;
            case 610:
                var2 = 8.0D;
                break;
            case 611:
                var2 = 8192.0D;
                break;
            case 612:
                var2 = 8388608.0D;
                break;
            case 613:
                var2 = 8.589934592E9D;
                break;
            case 614:
                var2 = 8.796093022208E12D;
                break;
            case 615:
                var2 = 9.007199254740992E15D;
                break;
            case 616:
                var2 = 9.223372036854776E18D;
                break;
            case 617:
                var2 = 9.44473296573929E21D;
                break;
            case 618:
                var2 = 9.671406556917033E24D;
                break;
            case 701:
                var2 = 1.0D;
                break;
            case 702:
                var2 = 1.6021766208E-19D;
                break;
            case 703:
                var2 = 1.6021766208000002E-16D;
                break;
            case 704:
                var2 = 1.6021766208000001E-13D;
                break;
            case 705:
                var2 = 1.6021766208000002E-10D;
                break;
            case 706:
                var2 = 1.6021766208000002E-7D;
                break;
            case 801:
                var2 = 1.0D;
                break;
            case 802:
                var2 = 0.2777777777777778D;
                break;
            case 803:
                var2 = 0.44704D;
                break;
            case 804:
                var2 = 0.514444444D;
                break;
            case 901:
                var2 = 1.0D;
                break;
            case 902:
                var2 = 7.716049382716049E-5D;
                break;
            case 903:
                var2 = 1.2417777777777778E-4D;
                break;
            case 1001:
                var2 = 1.0D;
                break;
            case 1002:
                var2 = 0.017453292519943295D;
                break;
            case 1003:
                var2 = 2.908882086657216E-4D;
                break;
            case 1004:
                var2 = 4.84813681109536E-6D;
        }

        this.setToNumber(var1, var2);
    }

    private void RANDOM_VARIABLE(int var1) {
        double var2 = 0.0D / 0.0;
        switch(((Token)this.tokensList.get(var1)).tokenId) {
            case 1:
                var2 = ProbabilityDistributions.rndUniformContinuous(ProbabilityDistributions.randomGenerator);
                break;
            case 2:
                var2 = (double)ProbabilityDistributions.rndInteger(ProbabilityDistributions.randomGenerator);
                break;
            case 3:
                var2 = ProbabilityDistributions.rndInteger(-10, 10, ProbabilityDistributions.randomGenerator);
                break;
            case 4:
                var2 = ProbabilityDistributions.rndInteger(-100, 100, ProbabilityDistributions.randomGenerator);
                break;
            case 5:
                var2 = ProbabilityDistributions.rndInteger(-1000, 1000, ProbabilityDistributions.randomGenerator);
                break;
            case 6:
                var2 = ProbabilityDistributions.rndInteger(-10000, 10000, ProbabilityDistributions.randomGenerator);
                break;
            case 7:
                var2 = ProbabilityDistributions.rndInteger(-100000, 100000, ProbabilityDistributions.randomGenerator);
                break;
            case 8:
                var2 = ProbabilityDistributions.rndInteger(-1000000, 1000000, ProbabilityDistributions.randomGenerator);
                break;
            case 9:
                var2 = ProbabilityDistributions.rndInteger(-10000000, 10000000, ProbabilityDistributions.randomGenerator);
                break;
            case 10:
                var2 = ProbabilityDistributions.rndInteger(-100000000, 100000000, ProbabilityDistributions.randomGenerator);
                break;
            case 11:
                var2 = ProbabilityDistributions.rndInteger(-1000000000, 1000000000, ProbabilityDistributions.randomGenerator);
                break;
            case 12:
                var2 = ProbabilityDistributions.rndInteger(0, 2147483646, ProbabilityDistributions.randomGenerator);
                break;
            case 13:
                var2 = ProbabilityDistributions.rndInteger(0, 10, ProbabilityDistributions.randomGenerator);
                break;
            case 14:
                var2 = ProbabilityDistributions.rndInteger(0, 100, ProbabilityDistributions.randomGenerator);
                break;
            case 15:
                var2 = ProbabilityDistributions.rndInteger(0, 1000, ProbabilityDistributions.randomGenerator);
                break;
            case 16:
                var2 = ProbabilityDistributions.rndInteger(0, 10000, ProbabilityDistributions.randomGenerator);
                break;
            case 17:
                var2 = ProbabilityDistributions.rndInteger(0, 100000, ProbabilityDistributions.randomGenerator);
                break;
            case 18:
                var2 = ProbabilityDistributions.rndInteger(0, 1000000, ProbabilityDistributions.randomGenerator);
                break;
            case 19:
                var2 = ProbabilityDistributions.rndInteger(0, 10000000, ProbabilityDistributions.randomGenerator);
                break;
            case 20:
                var2 = ProbabilityDistributions.rndInteger(0, 100000000, ProbabilityDistributions.randomGenerator);
                break;
            case 21:
                var2 = ProbabilityDistributions.rndInteger(0, 1000000000, ProbabilityDistributions.randomGenerator);
                break;
            case 22:
                var2 = ProbabilityDistributions.rndInteger(1, 2147483646, ProbabilityDistributions.randomGenerator);
                break;
            case 23:
                var2 = ProbabilityDistributions.rndInteger(1, 10, ProbabilityDistributions.randomGenerator);
                break;
            case 24:
                var2 = ProbabilityDistributions.rndInteger(1, 100, ProbabilityDistributions.randomGenerator);
                break;
            case 25:
                var2 = ProbabilityDistributions.rndInteger(1, 1000, ProbabilityDistributions.randomGenerator);
                break;
            case 26:
                var2 = ProbabilityDistributions.rndInteger(1, 10000, ProbabilityDistributions.randomGenerator);
                break;
            case 27:
                var2 = ProbabilityDistributions.rndInteger(1, 100000, ProbabilityDistributions.randomGenerator);
                break;
            case 28:
                var2 = ProbabilityDistributions.rndInteger(1, 1000000, ProbabilityDistributions.randomGenerator);
                break;
            case 29:
                var2 = ProbabilityDistributions.rndInteger(1, 10000000, ProbabilityDistributions.randomGenerator);
                break;
            case 30:
                var2 = ProbabilityDistributions.rndInteger(1, 100000000, ProbabilityDistributions.randomGenerator);
                break;
            case 31:
                var2 = ProbabilityDistributions.rndInteger(1, 1000000000, ProbabilityDistributions.randomGenerator);
                break;
            case 32:
                var2 = ProbabilityDistributions.rndNormal(0.0D, 1.0D, ProbabilityDistributions.randomGenerator);
        }

        this.setToNumber(var1, var2);
    }

    private double getTokenValue(int var1) {
        return ((Token)this.tokensList.get(var1)).tokenValue;
    }

    private void TETRATION(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, MathFunctions.tetration(var2, var4), true);
    }

    private void POWER(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, MathFunctions.power(var2, var4), true);
    }

    private void MODULO(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, MathFunctions.mod(var2, var4));
    }

    private void DIVIDE(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        if (this.disableRounding) {
            double var6 = 0.0D / 0.0;
            if (var4 != 0.0D) {
                var6 = var2 / var4;
            }

            this.opSetDecreaseRemove(var1, var6, true);
        } else {
            this.opSetDecreaseRemove(var1, MathFunctions.div(var2, var4), true);
        }

    }

    private void MULTIPLY(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        if (this.disableRounding) {
            this.opSetDecreaseRemove(var1, var2 * var4, true);
        } else {
            this.opSetDecreaseRemove(var1, MathFunctions.multiply(var2, var4), true);
        }

    }

    private void PLUS(int var1) {
        Token var2 = (Token)this.tokensList.get(var1 + 1);
        if (var1 > 0) {
            Token var3 = (Token)this.tokensList.get(var1 - 1);
            if (var3.tokenTypeId == 0 && var2.tokenTypeId == 0) {
                if (this.disableRounding) {
                    this.opSetDecreaseRemove(var1, var3.tokenValue + var2.tokenValue, true);
                } else {
                    this.opSetDecreaseRemove(var1, MathFunctions.plus(var3.tokenValue, var2.tokenValue), true);
                }
            } else if (var2.tokenTypeId == 0) {
                this.setToNumber(var1, var2.tokenValue);
                this.tokensList.remove(var1 + 1);
            }
        } else if (var2.tokenTypeId == 0) {
            this.setToNumber(var1, var2.tokenValue);
            this.tokensList.remove(var1 + 1);
        }

    }

    private void MINUS(int var1) {
        Token var2 = (Token)this.tokensList.get(var1 + 1);
        if (var1 > 0) {
            Token var3 = (Token)this.tokensList.get(var1 - 1);
            if (var3.tokenTypeId == 0 && var2.tokenTypeId == 0) {
                if (this.disableRounding) {
                    this.opSetDecreaseRemove(var1, var3.tokenValue - var2.tokenValue, true);
                } else {
                    this.opSetDecreaseRemove(var1, MathFunctions.minus(var3.tokenValue, var2.tokenValue), true);
                }
            } else if (var2.tokenTypeId == 0) {
                this.setToNumber(var1, -var2.tokenValue);
                this.tokensList.remove(var1 + 1);
            }
        } else if (var2.tokenTypeId == 0) {
            this.setToNumber(var1, -var2.tokenValue);
            this.tokensList.remove(var1 + 1);
        }

    }

    private void AND(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BooleanAlgebra.and(var2, var4));
    }

    private void OR(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BooleanAlgebra.or(var2, var4));
    }

    private void NAND(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BooleanAlgebra.nand(var2, var4));
    }

    private void NOR(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BooleanAlgebra.nor(var2, var4));
    }

    private void XOR(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BooleanAlgebra.xor(var2, var4));
    }

    private void IMP(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BooleanAlgebra.imp(var2, var4));
    }

    private void CIMP(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BooleanAlgebra.cimp(var2, var4));
    }

    private void NIMP(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BooleanAlgebra.nimp(var2, var4));
    }

    private void CNIMP(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BooleanAlgebra.cnimp(var2, var4));
    }

    private void EQV(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BooleanAlgebra.eqv(var2, var4));
    }

    private void NEG(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.setToNumber(var1, BooleanAlgebra.not(var2));
        this.tokensList.remove(var1 + 1);
    }

    private void EQ(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BinaryRelations.eq(var2, var4));
    }

    private void NEQ(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BinaryRelations.neq(var2, var4));
    }

    private void LT(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BinaryRelations.lt(var2, var4));
    }

    private void GT(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BinaryRelations.gt(var2, var4));
    }

    private void LEQ(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BinaryRelations.leq(var2, var4));
    }

    private void GEQ(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        double var4 = this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, BinaryRelations.geq(var2, var4));
    }

    private void BITWISE_COMPL(int var1) {
        long var2 = (long)this.getTokenValue(var1 + 1);
        this.setToNumber(var1, (double)(~var2));
        this.tokensList.remove(var1 + 1);
    }

    private void BITWISE_AND(int var1) {
        long var2 = (long)this.getTokenValue(var1 - 1);
        long var4 = (long)this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, (double)(var2 & var4));
    }

    private void BITWISE_OR(int var1) {
        long var2 = (long)this.getTokenValue(var1 - 1);
        long var4 = (long)this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, (double)(var2 | var4));
    }

    private void BITWISE_XOR(int var1) {
        long var2 = (long)this.getTokenValue(var1 - 1);
        long var4 = (long)this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, (double)(var2 ^ var4));
    }

    private void BITWISE_LEFT_SHIFT(int var1) {
        long var2 = (long)this.getTokenValue(var1 - 1);
        int var4 = (int)this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, (double)(var2 << var4));
    }

    private void BITWISE_RIGHT_SHIFT(int var1) {
        long var2 = (long)this.getTokenValue(var1 - 1);
        int var4 = (int)this.getTokenValue(var1 + 1);
        this.opSetDecreaseRemove(var1, (double)(var2 >> var4));
    }

    private void SIN(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.sin(var2));
    }

    private void COS(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.cos(var2));
    }

    private void TAN(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.tan(var2));
    }

    private void CTAN(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.ctan(var2));
    }

    private void SEC(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.sec(var2));
    }

    private void COSEC(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.cosec(var2));
    }

    private void ASIN(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.asin(var2));
    }

    private void ACOS(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.acos(var2));
    }

    private void ATAN(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.atan(var2));
    }

    private void ACTAN(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.actan(var2));
    }

    private void LN(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.ln(var2));
    }

    private void LOG2(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.log2(var2));
    }

    private void LOG10(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.log10(var2));
    }

    private void RAD(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.rad(var2));
    }

    private void EXP(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.exp(var2));
    }

    private void SQRT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.sqrt(var2));
    }

    private void SINH(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.sinh(var2));
    }

    private void COSH(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.cosh(var2));
    }

    private void TANH(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.tanh(var2));
    }

    private void COTH(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.coth(var2));
    }

    private void SECH(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.sech(var2));
    }

    private void CSCH(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.csch(var2));
    }

    private void DEG(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.deg(var2));
    }

    private void ABS(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.abs(var2));
    }

    private void SGN(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.sgn(var2));
    }

    private void FLOOR(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.floor(var2));
    }

    private void CEIL(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.ceil(var2));
    }

    private void ARSINH(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.arsinh(var2));
    }

    private void ARCOSH(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.arcosh(var2));
    }

    private void ARTANH(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.artanh(var2));
    }

    private void ARCOTH(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.arcoth(var2));
    }

    private void ARSECH(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.arsech(var2));
    }

    private void ARCSCH(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.arcsch(var2));
    }

    private void SA(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.sa(var2));
    }

    private void SINC(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.sinc(var2));
    }

    private void BELL_NUMBER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.bellNumber(var2));
    }

    private void LUCAS_NUMBER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.lucasNumber(var2));
    }

    private void FIBONACCI_NUMBER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.fibonacciNumber(var2));
    }

    private void HARMONIC_NUMBER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.harmonicNumber(var2));
    }

    private void IS_PRIME(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, NumberTheory.primeTest(var2));
    }

    private void PRIME_COUNT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, NumberTheory.primeCount(var2));
    }

    private void EXP_INT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.exponentialIntegralEi(var2));
    }

    private void LOG_INT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.logarithmicIntegralLi(var2));
    }

    private void OFF_LOG_INT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.offsetLogarithmicIntegralLi(var2));
    }

    private void FACT(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        this.setToNumber(var1, MathFunctions.factorial(var2));
        this.tokensList.remove(var1 - 1);
    }

    private void PERC(int var1) {
        double var2 = this.getTokenValue(var1 - 1);
        this.setToNumber(var1, var2 * 0.01D);
        this.tokensList.remove(var1 - 1);
    }

    private void NOT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, BooleanAlgebra.not(var2));
    }

    private void GAUSS_ERF(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.erf(var2));
    }

    private void GAUSS_ERFC(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.erfc(var2));
    }

    private void GAUSS_ERF_INV(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.erfInv(var2));
    }

    private void GAUSS_ERFC_INV(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.erfcInv(var2));
    }

    private void ULP(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.ulp(var2));
    }

    private void ISNAN(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        if (Double.isNaN(var2)) {
            this.f1SetDecreaseRemove(var1, 1.0D);
        } else {
            this.f1SetDecreaseRemove(var1, 0.0D);
        }

    }

    private void NDIG10(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, NumberTheory.numberOfDigits(var2));
    }

    private void NFACT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, NumberTheory.numberOfPrimeFactors(var2));
    }

    private void ARCSEC(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.asec(var2));
    }

    private void ARCCSC(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, MathFunctions.acosec(var2));
    }

    private void GAMMA(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.gamma(var2));
    }

    private void LAMBERT_W0(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.lambertW(var2, 0.0D));
    }

    private void LAMBERT_W1(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.lambertW(var2, -1.0D));
    }

    private void SGN_GAMMA(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.sgnGamma(var2));
    }

    private void LOG_GAMMA(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.logGamma(var2));
    }

    private void DI_GAMMA(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        this.f1SetDecreaseRemove(var1, SpecialFunctions.diGamma(var2));
    }

    private void UDF_PARAM(int var1) {
        double var2 = 0.0D / 0.0;
        double var4 = this.getTokenValue(var1 + 1);
        int var6 = this.UDFVariadicParamsAtRunTime.size();
        if (!Double.isNaN(var4) && var4 != 1.0D / 0.0 && var4 != -1.0D / 0.0) {
            int var7 = (int)MathFunctions.integerPart(var4);
            if (var7 == 0) {
                var2 = (double)var6;
            } else if (Math.abs(var7) <= var6) {
                if (var7 >= 1) {
                    var2 = (Double)this.UDFVariadicParamsAtRunTime.get(var7 - 1);
                } else if (var7 <= -1) {
                    var2 = (Double)this.UDFVariadicParamsAtRunTime.get(var6 + var7);
                }
            }
        }

        this.f1SetDecreaseRemove(var1, var2);
    }

    private void LOG(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.log(var4, var2));
    }

    private List<Double> getNumbers(int var1) {
        ArrayList var2 = new ArrayList();
        int var3 = var1;
        int var4 = this.tokensList.size() - 1;
        boolean var6 = false;

        do {
            ++var3;
            Token var7 = (Token)this.tokensList.get(var3);
            boolean var5 = false;
            if (var7.tokenTypeId == 0 && var7.tokenId == 1) {
                var5 = true;
                var2.add(var7.tokenValue);
            }

            if (var3 == var4 || !var5) {
                var6 = true;
            }
        } while(!var6);

        return var2;
    }

    private void MOD(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.mod(var2, var4));
    }

    private void BINOM_COEFF(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.binomCoeff(var2, var4));
    }

    private void PERMUTATIONS(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.numberOfPermutations(var2, var4));
    }

    private void BETA(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, SpecialFunctions.beta(var2, var4));
    }

    private void LOG_BETA(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, SpecialFunctions.logBeta(var2, var4));
    }

    private void BERNOULLI_NUMBER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.bernoulliNumber(var2, var4));
    }

    private void STIRLING1_NUMBER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.Stirling1Number(var2, var4));
    }

    private void STIRLING2_NUMBER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.Stirling2Number(var2, var4));
    }

    private void WORPITZKY_NUMBER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.worpitzkyNumber(var2, var4));
    }

    private void EULER_NUMBER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.eulerNumber(var2, var4));
    }

    private void KRONECKER_DELTA(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.kroneckerDelta(var2, var4));
    }

    private void EULER_POLYNOMIAL(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.eulerPolynomial(var2, var4));
    }

    private void HARMONIC2_NUMBER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.harmonicNumber(var2, var4));
    }

    private void ROUND(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        int var4 = (int)this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.round(var2, var4));
    }

    private void RND_VAR_UNIFORM_CONT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, ProbabilityDistributions.rndUniformContinuous(var2, var4, ProbabilityDistributions.randomGenerator));
    }

    private void RND_VAR_UNIFORM_DISCR(int var1) {
        int var2 = (int)this.getTokenValue(var1 + 1);
        int var3 = (int)this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, ProbabilityDistributions.rndInteger(var2, var3, ProbabilityDistributions.randomGenerator));
    }

    private void RND_NORMAL(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, ProbabilityDistributions.rndNormal(var2, var4, ProbabilityDistributions.randomGenerator));
    }

    private void NDIG(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, NumberTheory.numberOfDigits(var2, var4));
    }

    private void DIGIT10(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, NumberTheory.digitAtPosition(var2, var4));
    }

    private void FACTVAL(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, NumberTheory.primeFactorValue(var2, var4));
    }

    private void FACTEXP(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, NumberTheory.primeFactorExponent(var2, var4));
    }

    private void ROOT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, MathFunctions.root(var2, var4));
    }

    private void INC_GAMMA_LOWER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, SpecialFunctions.incompleteGammaLower(var2, var4));
    }

    private void INC_GAMMA_UPPER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, SpecialFunctions.incompleteGammaUpper(var2, var4));
    }

    private void REG_GAMMA_LOWER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, SpecialFunctions.regularizedGammaLowerP(var2, var4));
    }

    private void REG_GAMMA_UPPER(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        this.f2SetDecreaseRemove(var1, SpecialFunctions.regularizedGammaUpperQ(var2, var4));
    }

    private void IF_CONDITION(int var1) {
        List var2 = this.getFunctionParameters(var1, this.tokensList);
        FunctionParameter var3 = (FunctionParameter)var2.get(0);
        Expression var4 = new Expression(var3.paramStr, var3.tokens, this.argumentsList, this.functionsList, this.constantsList, false, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        if (this.verboseMode) {
            var4.setVerboseMode();
        }

        this.ifSetRemove(var1, var4.calculate());
    }

    private void IFF(int var1) {
        List var2 = this.getFunctionParameters(var1, this.tokensList);
        FunctionParameter var3 = (FunctionParameter)var2.get(0);
        int var4 = var2.size();
        int var6 = 1;
        double var8 = 0.0D;
        boolean var10 = true;

        do {
            Expression var7 = new Expression(var3.paramStr, var3.tokens, this.argumentsList, this.functionsList, this.constantsList, false, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
            if (this.verboseMode) {
                var7.setVerboseMode();
            }

            var10 = true;
            var8 = var7.calculate();
            if (var8 == 0.0D || Double.isNaN(var8)) {
                var6 += 2;
                var10 = false;
                if (var6 < var4) {
                    var3 = (FunctionParameter)var2.get(var6 - 1);
                }
            }
        } while(!var10 && var6 < var4);

        int var11;
        int var12;
        int var13;
        if (var10) {
            int var5 = var6 + 1;
            var11 = var1 + 1;
            var12 = ((FunctionParameter)var2.get(var4 - 1)).toIndex + 1;
            --((Token)this.tokensList.get(var11)).tokenLevel;
            --((Token)this.tokensList.get(var12)).tokenLevel;
            if (var5 < var4) {
                var12 = ((FunctionParameter)var2.get(var4 - 1)).toIndex;
                var11 = ((FunctionParameter)var2.get(var5)).fromIndex - 1;

                for(var13 = var12; var13 >= var11; --var13) {
                    this.tokensList.remove(var13);
                }
            }

            var11 = ((FunctionParameter)var2.get(var5 - 1)).fromIndex;
            var12 = ((FunctionParameter)var2.get(var5 - 1)).toIndex;

            for(var13 = var11; var13 <= var12; ++var13) {
                --((Token)this.tokensList.get(var13)).tokenLevel;
            }

            var12 = var11 - 1;
            var11 = var1;

            for(var13 = var12; var13 >= var11; --var13) {
                if (var13 != var1 + 1) {
                    this.tokensList.remove(var13);
                }
            }
        } else {
            var12 = ((FunctionParameter)var2.get(var4 - 1)).toIndex + 1;
            var11 = var1 + 1;

            for(var13 = var12; var13 >= var11; --var13) {
                this.tokensList.remove(var13);
            }

            this.setToNumber(var1, 0.0D / 0.0);
            --((Token)this.tokensList.get(var1)).tokenLevel;
        }

    }

    private void IF(int var1) {
        double var2 = ((Token)this.tokensList.get(var1 + 1)).tokenValue;
        double var4 = ((Token)this.tokensList.get(var1 + 2)).tokenValue;
        double var6 = ((Token)this.tokensList.get(var1 + 3)).tokenValue;
        double var8 = var6;
        if (var2 != 0.0D) {
            var8 = var4;
        }

        if (var2 == 0.0D / 0.0) {
            var8 = 0.0D / 0.0;
        }

        this.f3SetDecreaseRemove(var1, var8);
    }

    private void CHI(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, MathFunctions.chi(var2, var4, var6));
    }

    private void CHI_LR(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, MathFunctions.chi_LR(var2, var4, var6));
    }

    private void CHI_L(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, MathFunctions.chi_L(var2, var4, var6));
    }

    private void CHI_R(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, MathFunctions.chi_R(var2, var4, var6));
    }

    private void PDF_UNIFORM_CONT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, ProbabilityDistributions.pdfUniformContinuous(var2, var4, var6));
    }

    private void CDF_UNIFORM_CONT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, ProbabilityDistributions.cdfUniformContinuous(var2, var4, var6));
    }

    private void QNT_UNIFORM_CONT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, ProbabilityDistributions.qntUniformContinuous(var2, var4, var6));
    }

    private void PDF_NORMAL(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, ProbabilityDistributions.pdfNormal(var2, var4, var6));
    }

    private void CDF_NORMAL(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, ProbabilityDistributions.cdfNormal(var2, var4, var6));
    }

    private void QNT_NORMAL(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, ProbabilityDistributions.qntNormal(var2, var4, var6));
    }

    private void DIGIT(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, NumberTheory.digitAtPosition(var2, var4, var6));
    }

    private void INC_BETA(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, SpecialFunctions.incompleteBeta(var4, var6, var2));
    }

    private void REG_BETA(int var1) {
        double var2 = this.getTokenValue(var1 + 1);
        double var4 = this.getTokenValue(var1 + 2);
        double var6 = this.getTokenValue(var1 + 3);
        this.f3SetDecreaseRemove(var1, SpecialFunctions.regularizedBeta(var4, var6, var2));
    }

    private void updateMissingTokens(List<Token> var1, String var2, int var3, int var4) {
        Iterator var5 = var1.iterator();

        while(var5.hasNext()) {
            Token var6 = (Token)var5.next();
            if (var6.tokenTypeId == -1 && var6.tokenStr.equals(var2)) {
                var6.keyWord = var2;
                var6.tokenId = var3;
                var6.tokenTypeId = var4;
            }
        }

    }

    private void updateMissingTokens(ArgumentParameter var1, IterativeOperatorParameters var2) {
        if (var1.presence == -1) {
            this.updateMissingTokens(var2.indexParam.tokens, var2.indexParam.paramStr, var1.index, 101);
            this.updateMissingTokens(var2.fromParam.tokens, var2.indexParam.paramStr, var1.index, 101);
            this.updateMissingTokens(var2.toParam.tokens, var2.indexParam.paramStr, var1.index, 101);
            this.updateMissingTokens(var2.funParam.tokens, var2.indexParam.paramStr, var1.index, 101);
        }

    }

    private void evalFromToDeltaParameters(ArgumentParameter var1, IterativeOperatorParameters var2) {
        var2.fromExp = new Expression(var2.fromParam.paramStr, var2.fromParam.tokens, this.argumentsList, this.functionsList, this.constantsList, false, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        var2.toExp = new Expression(var2.toParam.paramStr, var2.toParam.tokens, this.argumentsList, this.functionsList, this.constantsList, false, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        var2.funExp = new Expression(var2.funParam.paramStr, var2.funParam.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        var2.deltaExp = null;
        if (this.verboseMode) {
            var2.fromExp.setVerboseMode();
            var2.toExp.setVerboseMode();
            var2.funExp.setVerboseMode();
        }

        var2.from = var2.fromExp.calculate();
        var2.to = var2.toExp.calculate();
        var2.delta = 1.0D;
        if (var2.to < var2.from) {
            var2.delta = -1.0D;
        }

        if (var2.withDelta) {
            var2.deltaExp = new Expression(var2.deltaParam.paramStr, var2.deltaParam.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
            if (var1.presence == -1) {
                this.updateMissingTokens(var2.deltaParam.tokens, var2.indexParam.paramStr, var1.index, 101);
            }

            if (this.verboseMode) {
                var2.deltaExp.setVerboseMode();
            }

            var2.delta = var2.deltaExp.calculate();
        }

    }

    private void SUM(int var1) {
        IterativeOperatorParameters var2 = new IterativeOperatorParameters(this.getFunctionParameters(var1, this.tokensList));
        ArgumentParameter var3 = this.getParamArgument(var2.indexParam.paramStr);
        this.updateMissingTokens(var3, var2);
        this.evalFromToDeltaParameters(var3, var2);
        double var4 = NumberTheory.sigmaSummation(var2.funExp, var3.argument, var2.from, var2.to, var2.delta);
        this.clearParamArgument(var3);
        this.calcSetDecreaseRemove(var1, var4, true);
    }

    private void PROD(int var1) {
        IterativeOperatorParameters var2 = new IterativeOperatorParameters(this.getFunctionParameters(var1, this.tokensList));
        ArgumentParameter var3 = this.getParamArgument(var2.indexParam.paramStr);
        this.updateMissingTokens(var3, var2);
        this.evalFromToDeltaParameters(var3, var2);
        double var4 = NumberTheory.piProduct(var2.funExp, var3.argument, var2.from, var2.to, var2.delta);
        this.clearParamArgument(var3);
        this.calcSetDecreaseRemove(var1, var4, true);
    }

    private void MIN(int var1) {
        IterativeOperatorParameters var2 = new IterativeOperatorParameters(this.getFunctionParameters(var1, this.tokensList));
        ArgumentParameter var3 = this.getParamArgument(var2.indexParam.paramStr);
        this.updateMissingTokens(var3, var2);
        this.evalFromToDeltaParameters(var3, var2);
        double var4 = NumberTheory.min(var2.funExp, var3.argument, var2.from, var2.to, var2.delta);
        this.clearParamArgument(var3);
        this.calcSetDecreaseRemove(var1, var4);
    }

    private void MAX(int var1) {
        IterativeOperatorParameters var2 = new IterativeOperatorParameters(this.getFunctionParameters(var1, this.tokensList));
        ArgumentParameter var3 = this.getParamArgument(var2.indexParam.paramStr);
        this.updateMissingTokens(var3, var2);
        this.evalFromToDeltaParameters(var3, var2);
        double var4 = NumberTheory.max(var2.funExp, var3.argument, var2.from, var2.to, var2.delta);
        this.clearParamArgument(var3);
        this.calcSetDecreaseRemove(var1, var4);
    }

    private void AVG(int var1) {
        IterativeOperatorParameters var2 = new IterativeOperatorParameters(this.getFunctionParameters(var1, this.tokensList));
        ArgumentParameter var3 = this.getParamArgument(var2.indexParam.paramStr);
        this.updateMissingTokens(var3, var2);
        this.evalFromToDeltaParameters(var3, var2);
        double var4 = Statistics.avg(var2.funExp, var3.argument, var2.from, var2.to, var2.delta);
        this.clearParamArgument(var3);
        this.calcSetDecreaseRemove(var1, var4, true);
    }

    private void VAR(int var1) {
        IterativeOperatorParameters var2 = new IterativeOperatorParameters(this.getFunctionParameters(var1, this.tokensList));
        ArgumentParameter var3 = this.getParamArgument(var2.indexParam.paramStr);
        this.updateMissingTokens(var3, var2);
        this.evalFromToDeltaParameters(var3, var2);
        double var4 = Statistics.var(var2.funExp, var3.argument, var2.from, var2.to, var2.delta);
        this.clearParamArgument(var3);
        this.calcSetDecreaseRemove(var1, var4, true);
    }

    private void STD(int var1) {
        IterativeOperatorParameters var2 = new IterativeOperatorParameters(this.getFunctionParameters(var1, this.tokensList));
        ArgumentParameter var3 = this.getParamArgument(var2.indexParam.paramStr);
        this.updateMissingTokens(var3, var2);
        this.evalFromToDeltaParameters(var3, var2);
        double var4 = Statistics.std(var2.funExp, var3.argument, var2.from, var2.to, var2.delta);
        this.clearParamArgument(var3);
        this.calcSetDecreaseRemove(var1, var4, true);
    }

    private void DERIVATIVE(int var1, int var2) {
        List var3 = this.getFunctionParameters(var1, this.tokensList);
        FunctionParameter var7 = (FunctionParameter)var3.get(0);
        FunctionParameter var8 = (FunctionParameter)var3.get(1);
        ArgumentParameter var9 = this.getParamArgument(var8.paramStr);
        if (var9.presence == -1) {
            this.updateMissingTokens(var8.tokens, var8.paramStr, var9.index, 101);
            this.updateMissingTokens(var7.tokens, var8.paramStr, var9.index, 101);
        }

        Expression var10 = new Expression(var7.paramStr, var7.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        double var11 = 0.0D / 0.0;
        if (var3.size() == 2 || var3.size() == 4) {
            var11 = var9.argument.getArgumentValue();
        }

        if (var3.size() == 3 || var3.size() == 5) {
            FunctionParameter var13 = (FunctionParameter)var3.get(2);
            if (var9.presence == -1) {
                this.updateMissingTokens(var13.tokens, var8.paramStr, var9.index, 101);
            }

            Expression var14 = new Expression(var13.paramStr, var13.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
            var11 = var14.calculate();
        }

        double var20 = 1.0E-8D;
        int var15 = 20;
        if (var3.size() == 4 || var3.size() == 5) {
            FunctionParameter var16;
            FunctionParameter var17;
            if (var3.size() == 4) {
                var16 = (FunctionParameter)var3.get(2);
                var17 = (FunctionParameter)var3.get(3);
            } else {
                var16 = (FunctionParameter)var3.get(3);
                var17 = (FunctionParameter)var3.get(4);
            }

            if (var9.presence == -1) {
                this.updateMissingTokens(var16.tokens, var8.paramStr, var9.index, 101);
                this.updateMissingTokens(var17.tokens, var8.paramStr, var9.index, 101);
            }

            Expression var18 = new Expression(var16.paramStr, var16.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
            Expression var19 = new Expression(var17.paramStr, var17.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
            var20 = var18.calculate();
            var15 = (int)Math.round(var19.calculate());
        }

        double var21;
        if (var2 == 3) {
            var21 = Calculus.derivative(var10, var9.argument, var11, 3, var20, var15);
            this.calcSetDecreaseRemove(var1, var21);
        } else if (var2 == 1) {
            var21 = Calculus.derivative(var10, var9.argument, var11, 1, var20, var15);
            this.calcSetDecreaseRemove(var1, var21);
        } else {
            var21 = Calculus.derivative(var10, var9.argument, var11, 2, var20, var15);
            this.calcSetDecreaseRemove(var1, var21);
        }

        this.clearParamArgument(var9);
    }

    private void DERIVATIVE_NTH(int var1, int var2) {
        List var6 = this.getFunctionParameters(var1, this.tokensList);
        FunctionParameter var7 = (FunctionParameter)var6.get(0);
        FunctionParameter var8 = (FunctionParameter)var6.get(1);
        FunctionParameter var9 = (FunctionParameter)var6.get(2);
        ArgumentParameter var10 = this.getParamArgument(var9.paramStr);
        if (var10.presence == -1) {
            this.updateMissingTokens(var9.tokens, var9.paramStr, var10.index, 101);
            this.updateMissingTokens(var7.tokens, var9.paramStr, var10.index, 101);
            this.updateMissingTokens(var8.tokens, var9.paramStr, var10.index, 101);
        }

        Expression var11 = new Expression(var7.paramStr, var7.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        Expression var12 = new Expression(var8.paramStr, var8.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        double var13 = var12.calculate();
        double var15 = var10.argument.getArgumentValue();
        double var17 = 1.0E-6D;
        int var19 = 20;
        if (var6.size() == 5) {
            FunctionParameter var20 = (FunctionParameter)var6.get(3);
            FunctionParameter var21 = (FunctionParameter)var6.get(4);
            if (var10.presence == -1) {
                this.updateMissingTokens(var20.tokens, var9.paramStr, var10.index, 101);
                this.updateMissingTokens(var21.tokens, var9.paramStr, var10.index, 101);
            }

            Expression var22 = new Expression(var20.paramStr, var20.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
            Expression var23 = new Expression(var21.paramStr, var21.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
            var17 = var22.calculate();
            var19 = (int)Math.round(var23.calculate());
        }

        double var25;
        if (var2 == 3) {
            var25 = Calculus.derivativeNth(var11, var13, var10.argument, var15, 1, var17, var19);
            double var24 = Calculus.derivativeNth(var11, var13, var10.argument, var15, 2, var17, var19);
            this.calcSetDecreaseRemove(var1, (var25 + var24) / 2.0D);
        } else if (var2 == 1) {
            var25 = Calculus.derivativeNth(var11, var13, var10.argument, var15, 1, var17, var19);
            this.calcSetDecreaseRemove(var1, var25);
        } else {
            var25 = Calculus.derivativeNth(var11, var13, var10.argument, var15, 2, var17, var19);
            this.calcSetDecreaseRemove(var1, var25);
        }

        this.clearParamArgument(var10);
    }

    private void INTEGRAL(int var1) {
        List var5 = this.getFunctionParameters(var1, this.tokensList);
        FunctionParameter var6 = (FunctionParameter)var5.get(0);
        FunctionParameter var7 = (FunctionParameter)var5.get(1);
        FunctionParameter var8 = (FunctionParameter)var5.get(2);
        FunctionParameter var9 = (FunctionParameter)var5.get(3);
        ArgumentParameter var10 = this.getParamArgument(var7.paramStr);
        if (var10.presence == -1) {
            this.updateMissingTokens(var7.tokens, var7.paramStr, var10.index, 101);
            this.updateMissingTokens(var6.tokens, var7.paramStr, var10.index, 101);
            this.updateMissingTokens(var8.tokens, var7.paramStr, var10.index, 101);
            this.updateMissingTokens(var9.tokens, var7.paramStr, var10.index, 101);
        }

        Expression var11 = new Expression(var6.paramStr, var6.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        Expression var12 = new Expression(var8.paramStr, var8.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        Expression var13 = new Expression(var9.paramStr, var9.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        double var14 = 1.0E-6D;
        byte var16 = 20;
        this.calcSetDecreaseRemove(var1, Calculus.integralTrapezoid(var11, var10.argument, var12.calculate(), var13.calculate(), var14, var16));
        this.clearParamArgument(var10);
    }

    private void SOLVE(int var1) {
        List var5 = this.getFunctionParameters(var1, this.tokensList);
        FunctionParameter var6 = (FunctionParameter)var5.get(0);
        FunctionParameter var7 = (FunctionParameter)var5.get(1);
        FunctionParameter var8 = (FunctionParameter)var5.get(2);
        FunctionParameter var9 = (FunctionParameter)var5.get(3);
        ArgumentParameter var10 = this.getParamArgument(var7.paramStr);
        if (var10.presence == -1) {
            this.updateMissingTokens(var7.tokens, var7.paramStr, var10.index, 101);
            this.updateMissingTokens(var6.tokens, var7.paramStr, var10.index, 101);
            this.updateMissingTokens(var8.tokens, var7.paramStr, var10.index, 101);
            this.updateMissingTokens(var9.tokens, var7.paramStr, var10.index, 101);
        }

        Expression var11 = new Expression(var6.paramStr, var6.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        Expression var12 = new Expression(var8.paramStr, var8.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        Expression var13 = new Expression(var9.paramStr, var9.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        double var14 = 1.0E-9D;
        byte var16 = 100;
        this.calcSetDecreaseRemove(var1, Calculus.solveBrent(var11, var10.argument, var12.calculate(), var13.calculate(), var14, (double)var16));
        this.clearParamArgument(var10);
    }

    private void FORWARD_DIFFERENCE(int var1) {
        List var2 = this.getFunctionParameters(var1, this.tokensList);
        FunctionParameter var3 = (FunctionParameter)var2.get(0);
        FunctionParameter var4 = (FunctionParameter)var2.get(1);
        ArgumentParameter var5 = this.getParamArgument(var4.paramStr);
        Expression var6 = new Expression(var3.paramStr, var3.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        if (this.verboseMode) {
            var6.setVerboseMode();
        }

        double var7 = 1.0D;
        if (var2.size() == 3) {
            FunctionParameter var9 = (FunctionParameter)var2.get(2);
            Expression var10 = new Expression(var9.paramStr, var9.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
            if (this.verboseMode) {
                var10.setVerboseMode();
            }

            var7 = var10.calculate();
        }

        this.calcSetDecreaseRemove(var1, Calculus.forwardDifference(var6, var7, var5.argument));
        this.clearParamArgument(var5);
    }

    private void BACKWARD_DIFFERENCE(int var1) {
        List var2 = this.getFunctionParameters(var1, this.tokensList);
        FunctionParameter var3 = (FunctionParameter)var2.get(0);
        FunctionParameter var4 = (FunctionParameter)var2.get(1);
        ArgumentParameter var5 = this.getParamArgument(var4.paramStr);
        Expression var6 = new Expression(var3.paramStr, var3.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
        if (this.verboseMode) {
            var6.setVerboseMode();
        }

        double var7 = 1.0D;
        if (var2.size() == 3) {
            FunctionParameter var9 = (FunctionParameter)var2.get(2);
            Expression var10 = new Expression(var9.paramStr, var9.tokens, this.argumentsList, this.functionsList, this.constantsList, true, this.UDFExpression, this.UDFVariadicParamsAtRunTime);
            if (this.verboseMode) {
                var10.setVerboseMode();
            }

            var7 = var10.calculate();
        }

        this.calcSetDecreaseRemove(var1, Calculus.backwardDifference(var6, var7, var5.argument));
        this.clearParamArgument(var5);
    }

    private void MIN_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, NumberTheory.min(mXparser.arrayList2double(var2)), var2.size());
    }

    private void MAX_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, NumberTheory.max(mXparser.arrayList2double(var2)), var2.size());
    }

    private void SUM_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, NumberTheory.sum(mXparser.arrayList2double(var2)), var2.size(), true);
    }

    private void PROD_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, NumberTheory.prod(mXparser.arrayList2double(var2)), var2.size(), true);
    }

    private void AVG_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, Statistics.avg(mXparser.arrayList2double(var2)), var2.size(), true);
    }

    private void VAR_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, Statistics.var(mXparser.arrayList2double(var2)), var2.size(), true);
    }

    private void STD_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, Statistics.std(mXparser.arrayList2double(var2)), var2.size(), true);
    }

    private void CONTINUED_FRACTION(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, MathFunctions.continuedFraction(mXparser.arrayList2double(var2)), var2.size());
    }

    private void CONTINUED_POLYNOMIAL(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, MathFunctions.continuedPolynomial(mXparser.arrayList2double(var2)), var2.size());
    }

    private void GCD(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, NumberTheory.gcd(mXparser.arrayList2double(var2)), var2.size());
    }

    private void LCM(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, NumberTheory.lcm(mXparser.arrayList2double(var2)), var2.size());
    }

    private void RND_LIST(int var1) {
        List var2 = this.getNumbers(var1);
        int var3 = var2.size();
        int var4 = ProbabilityDistributions.rndIndex(var3, ProbabilityDistributions.randomGenerator);
        this.variadicSetDecreaseRemove(var1, (Double)var2.get(var4), var2.size());
    }

    private void COALESCE(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, MathFunctions.coalesce(mXparser.arrayList2double(var2)), var2.size());
    }

    private void OR_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, BooleanAlgebra.orVariadic(mXparser.arrayList2double(var2)), var2.size());
    }

    private void AND_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, BooleanAlgebra.andVariadic(mXparser.arrayList2double(var2)), var2.size());
    }

    private void XOR_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, BooleanAlgebra.xorVariadic(mXparser.arrayList2double(var2)), var2.size());
    }

    private void ARGMIN_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, NumberTheory.argmin(mXparser.arrayList2double(var2)), var2.size());
    }

    private void ARGMAX_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, NumberTheory.argmax(mXparser.arrayList2double(var2)), var2.size());
    }

    private void MEDIAN_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, Statistics.median(mXparser.arrayList2double(var2)), var2.size());
    }

    private void MODE_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, Statistics.mode(mXparser.arrayList2double(var2)), var2.size());
    }

    private void BASE_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, NumberTheory.convOthBase2Decimal(mXparser.arrayList2double(var2)), var2.size());
    }

    private void NDIST_VARIADIC(int var1) {
        List var2 = this.getNumbers(var1);
        this.variadicSetDecreaseRemove(var1, NumberTheory.numberOfDistValues(mXparser.arrayList2double(var2)), var2.size());
    }

    private void COMMA(int var1) {
        this.tokensList.remove(var1);
    }

    private void PARENTHESES(int var1, int var2) {
        for(int var3 = var1; var3 <= var2; ++var3) {
            --((Token)this.tokensList.get(var3)).tokenLevel;
        }

        this.tokensList.remove(var2);
        this.tokensList.remove(var1);
    }

    public boolean checkLexSyntax() {
        boolean var1 = true;
        this.recursionCallsCounter = 0;
        if (this.expressionString.length() == 0) {
            var1 = false;
            this.errorMessage = "Empty expression string\n";
            return var1;
        } else {
            SyntaxChecker var2 = new SyntaxChecker(new ByteArrayInputStream(this.expressionString.getBytes()));

            try {
                var2.checkSyntax();
            } catch (Exception var4) {
                var1 = false;
                this.errorMessage = "lexical error \n\n" + var4.getMessage() + "\n";
            }

            return var1;
        }
    }

    public boolean checkSyntax() {
        boolean var1 = this.checkSyntax("[" + this.expressionString + "] ", false);
        return var1;
    }

    private int checkCalculusParameter(String var1) {
        int var2 = 0;
        Iterator var3 = this.keyWordsList.iterator();

        while(var3.hasNext()) {
            KeyWord var4 = (KeyWord)var3.next();
            if (var4.wordTypeId != 101 && var1.equals(var4.wordString)) {
                ++var2;
            }
        }

        return var2;
    }

    private boolean checkIfKnownArgument(FunctionParameter var1) {
        if (var1.tokens.size() > 1) {
            return false;
        } else {
            Token var2 = (Token)var1.tokens.get(0);
            return var2.tokenTypeId == 101;
        }
    }

    private boolean checkIfUnknownToken(FunctionParameter var1) {
        if (var1.tokens.size() > 1) {
            return false;
        } else {
            Token var2 = (Token)var1.tokens.get(0);
            return var2.tokenTypeId == -1;
        }
    }

    private boolean checkSyntax(String var1, boolean var2) {
        if (!this.expressionWasModified && this.syntaxStatus && this.optionsChangesetNumber == mXparser.optionsChangesetNumber) {
            this.errorMessage = var1 + "already checked - no errors!\n";
            this.recursionCallPending = false;
            return true;
        } else {
            this.optionsChangesetNumber = mXparser.optionsChangesetNumber;
            if (var2) {
                this.syntaxStatus = true;
                this.recursionCallPending = false;
                this.expressionWasModified = false;
                this.errorMessage = this.errorMessage + var1 + "function with extended body - assuming no errors.\n";
                return true;
            } else {
                this.recursionCallPending = true;
                this.errorMessage = var1 + "checking ...\n";
                boolean var3 = true;
                if (this.expressionString.length() == 0) {
                    var3 = false;
                    this.errorMessage = this.errorMessage + var1 + "Empty expression string\n";
                    this.syntaxStatus = var3;
                    this.recursionCallPending = false;
                    return var3;
                } else {
                    SyntaxChecker var4 = new SyntaxChecker(new ByteArrayInputStream(this.expressionString.getBytes()));

                    try {
                        var4.checkSyntax();
                        this.tokenizeExpressionString();
                        Collections.sort(this.keyWordsList, new KwStrComparator());

                        int var7;
                        for(var7 = 1; var7 < this.keyWordsList.size(); ++var7) {
                            String var5 = ((KeyWord)this.keyWordsList.get(var7 - 1)).wordString;
                            String var6 = ((KeyWord)this.keyWordsList.get(var7)).wordString;
                            if (var5.equals(var6)) {
                                var3 = false;
                                this.errorMessage = this.errorMessage + var1 + "(" + var5 + ") Duplicated <KEYWORD>.\n";
                            }
                        }

                        var7 = this.initialTokens.size();
                        Stack var8 = new Stack();

                        for(int var10 = 0; var10 < var7; ++var10) {
                            Token var11 = (Token)this.initialTokens.get(var10);
                            String var12 = "(" + var11.tokenStr + ", " + var10 + ") ";
                            Argument var13;
                            boolean var14;
                            if (var11.tokenTypeId == 101) {
                                var13 = this.getArgument(var11.tokenId);
                                if (this.getParametersNumber(var10) >= 0) {
                                    var3 = false;
                                    this.errorMessage = this.errorMessage + var1 + var12 + "<ARGUMENT> was expected.\n";
                                } else if (var13.getArgumentBodyType() == 1) {
                                    if (var13.getArgumentType() == 2 && var13.argumentExpression != this && !var13.argumentExpression.recursionCallPending) {
                                        var14 = var13.argumentExpression.checkSyntax(var1 + "-> [" + var11.tokenStr + "] = [" + var13.argumentExpression.getExpressionString() + "] ", false);
                                        var3 = var3 && var14;
                                        this.errorMessage = this.errorMessage + var1 + var12 + "checking dependent argument ...\n" + var13.argumentExpression.getErrorMessage();
                                    }
                                } else {
                                    this.errorMessage = this.errorMessage + var1 + var12 + "argument with extended body - assuming no errors.\n";
                                }
                            }

                            if (var11.tokenTypeId == 102) {
                                var13 = this.getArgument(var11.tokenId);
                                if (this.getParametersNumber(var10) != 1) {
                                    var3 = false;
                                    this.errorMessage = this.errorMessage + var1 + var12 + "<RECURSIVE_ARGUMENT> expecting 1 parameter.\n";
                                } else if (var13.argumentExpression != this && !var13.argumentExpression.recursionCallPending) {
                                    var14 = var13.argumentExpression.checkSyntax(var1 + "-> [" + var11.tokenStr + "] = [" + var13.argumentExpression.getExpressionString() + "] ", false);
                                    var3 = var3 && var14;
                                    this.errorMessage = this.errorMessage + var1 + var12 + "checking recursive argument ...\n" + var13.argumentExpression.getErrorMessage();
                                }
                            }

                            if (var11.tokenTypeId == -1) {
                                boolean var18 = false;
                                Iterator var20 = var8.iterator();

                                while(var20.hasNext()) {
                                    SyntaxStackElement var15 = (SyntaxStackElement)var20.next();
                                    if (var15.tokenStr.equals(var11.tokenStr)) {
                                        var18 = true;
                                    }
                                }

                                if (!var18) {
                                    var3 = false;
                                    this.errorMessage = this.errorMessage + var1 + var12 + "invalid <TOKEN>.\n";
                                }
                            }

                            if (var11.tokenTypeId == 103) {
                                Function var19 = this.getFunction(var11.tokenId);
                                var19.checkRecursiveMode();
                                int var22 = this.getParametersNumber(var10);
                                int var23 = var19.getParametersNumber();
                                if (var22 == 0) {
                                    var3 = false;
                                    this.errorMessage = this.errorMessage + var1 + var12 + "<USER_DEFINED_FUNCTION> expecting at least one argument.\n";
                                } else if (!var19.isVariadic && var23 != var22) {
                                    var3 = false;
                                    this.errorMessage = this.errorMessage + var1 + var12 + "<USER_DEFINED_FUNCTION> expecting " + var23 + " arguments.\n";
                                } else if (var19.functionExpression != this && !var19.functionExpression.recursionCallPending) {
                                    boolean var16;
                                    if (var19.getFunctionBodyType() == 1) {
                                        var16 = var19.functionExpression.checkSyntax(var1 + "-> [" + var11.tokenStr + "] = [" + var19.functionExpression.getExpressionString() + "] ", false);
                                    } else {
                                        var16 = var19.functionExpression.checkSyntax(var1 + "-> [" + var11.tokenStr + "] = [" + var19.functionExpression.getExpressionString() + "] ", true);
                                    }

                                    var3 = var3 && var16;
                                    if (var19.isVariadic) {
                                        this.errorMessage = this.errorMessage + var1 + var12 + "checking variadic user defined function ...\n" + var19.functionExpression.getErrorMessage();
                                    } else {
                                        this.errorMessage = this.errorMessage + var1 + var12 + "checking user defined function ...\n" + var19.functionExpression.getErrorMessage();
                                    }
                                }
                            }

                            if (var11.tokenTypeId == 9 && this.getParametersNumber(var10) >= 0) {
                                var3 = false;
                                this.errorMessage = this.errorMessage + var1 + var12 + "<CONSTANT> was expected.\n";
                            }

                            if (var11.tokenTypeId == 104 && this.getParametersNumber(var10) >= 0) {
                                var3 = false;
                                this.errorMessage = this.errorMessage + var1 + var12 + "<USER_DEFINED_CONSTANT> was expected.\n";
                            }

                            if (var11.tokenTypeId == 4 && this.getParametersNumber(var10) != 1) {
                                var3 = false;
                                this.errorMessage = this.errorMessage + var1 + var12 + "<FUNCTION> expecting 1 argument.\n";
                            }

                            if (var11.tokenTypeId == 5 && this.getParametersNumber(var10) != 2) {
                                var3 = false;
                                this.errorMessage = this.errorMessage + var1 + var12 + "<FUNCTION> expecting 2 arguments.\n";
                            }

                            if (var11.tokenTypeId == 6 && this.getParametersNumber(var10) != 3) {
                                var3 = false;
                                this.errorMessage = this.errorMessage + var1 + var12 + "<FUNCTION> expecting 3 arguments.\n";
                            }

                            int var21;
                            if (var11.tokenTypeId == 8) {
                                var21 = this.getParametersNumber(var10);
                                List var24 = null;
                                if (var21 > 0) {
                                    var24 = this.getFunctionParameters(var10, this.initialTokens);
                                }

                                SyntaxStackElement var9;
                                FunctionParameter var25;
                                int var26;
                                if (var11.tokenId == 6 || var11.tokenId == 7 || var11.tokenId == 8) {
                                    if (var21 >= 2 && var21 <= 5) {
                                        if (var21 != 2 && var21 != 4) {
                                            var25 = (FunctionParameter)var24.get(1);
                                            var9 = new SyntaxStackElement(var25.paramStr, var11.tokenLevel + 1);
                                            var8.push(var9);
                                            var26 = this.checkCalculusParameter(var9.tokenStr);
                                            if (var26 > 0) {
                                                var3 = false;
                                                this.errorMessage = this.errorMessage + var1 + var12 + "<DERIVATIVE> Found duplicated key words for calculus parameter (" + var9.tokenStr + ", " + var26 + ").\n";
                                            }

                                            if (!this.checkIfKnownArgument(var25) && !this.checkIfUnknownToken(var25)) {
                                                var3 = false;
                                                this.errorMessage = this.errorMessage + var1 + var12 + "<DERIVATIVE> One token (argument or unknown) was expected.\n";
                                            }
                                        } else {
                                            var25 = (FunctionParameter)var24.get(1);
                                            if (!this.checkIfKnownArgument(var25)) {
                                                var3 = false;
                                                this.errorMessage = this.errorMessage + var1 + var12 + "<DERIVATIVE> argument was expected.\n";
                                            }
                                        }
                                    } else {
                                        var3 = false;
                                        this.errorMessage = this.errorMessage + var1 + var12 + "<DERIVATIVE> expecting 2 or 3 or 4 or 5 calculus parameters.\n";
                                    }
                                }

                                if (var11.tokenId == 9) {
                                    if (var21 != 3 && var21 != 5) {
                                        var3 = false;
                                        this.errorMessage = this.errorMessage + var1 + var12 + "<NTH_DERIVATIVE> expecting 3 or 5 calculus arguments.\n";
                                    } else {
                                        var25 = (FunctionParameter)var24.get(2);
                                        if (!this.checkIfKnownArgument(var25)) {
                                            var3 = false;
                                            this.errorMessage = this.errorMessage + var1 + var12 + "<DERIVATIVE> argument was expected.\n";
                                        }
                                    }
                                }

                                if (var11.tokenId == 5 || var11.tokenId == 17) {
                                    if (var21 != 4) {
                                        var3 = false;
                                        this.errorMessage = this.errorMessage + var1 + var12 + "<INTEGRAL/SOLVE> expecting 4 calculus arguments.\n";
                                    } else {
                                        var25 = (FunctionParameter)var24.get(1);
                                        var9 = new SyntaxStackElement(var25.paramStr, var11.tokenLevel + 1);
                                        var8.push(var9);
                                        var26 = this.checkCalculusParameter(var9.tokenStr);
                                        if (var26 > 0) {
                                            var3 = false;
                                            this.errorMessage = this.errorMessage + var1 + var12 + "Found duplicated key words for calculus parameter (" + var9.tokenStr + ", " + var26 + ").\n";
                                        }

                                        if (!this.checkIfKnownArgument(var25) && !this.checkIfUnknownToken(var25)) {
                                            var3 = false;
                                            this.errorMessage = this.errorMessage + var1 + var12 + "One token (argument or unknown) was expected.\n";
                                        }
                                    }
                                }

                                if (var11.tokenId == 3 || var11.tokenId == 1 || var11.tokenId == 15 || var11.tokenId == 16 || var11.tokenId == 12 || var11.tokenId == 13 || var11.tokenId == 14) {
                                    if (var21 != 4 && var21 != 5) {
                                        var3 = false;
                                        this.errorMessage = this.errorMessage + var1 + var12 + "<ITER_OPERATOR> expecting 4 or 5 calculus arguments.\n";
                                    } else {
                                        var25 = (FunctionParameter)var24.get(0);
                                        var9 = new SyntaxStackElement(var25.paramStr, var11.tokenLevel + 1);
                                        var8.push(var9);
                                        var26 = this.checkCalculusParameter(var9.tokenStr);
                                        if (var26 > 0) {
                                            var3 = false;
                                            this.errorMessage = this.errorMessage + var1 + var12 + "Found duplicated key words for calculus parameter (" + var9.tokenStr + ", " + var26 + ").\n";
                                        }

                                        if (!this.checkIfKnownArgument(var25) && !this.checkIfUnknownToken(var25)) {
                                            var3 = false;
                                            this.errorMessage = this.errorMessage + var1 + var12 + "One token (argument or unknown) was expected.\n";
                                        }
                                    }
                                }

                                if (var11.tokenId == 10 || var11.tokenId == 11) {
                                    if (var21 != 2 && var21 != 3) {
                                        var3 = false;
                                        this.errorMessage = this.errorMessage + var1 + var12 + "<DIFF> expecting 2 or 3 arguments.\n";
                                    } else {
                                        var25 = (FunctionParameter)var24.get(1);
                                        if (!this.checkIfKnownArgument(var25)) {
                                            var3 = false;
                                            this.errorMessage = this.errorMessage + var1 + var12 + "<DIFF> argument was expected.\n";
                                        }
                                    }
                                }
                            }

                            if (var11.tokenTypeId == 7) {
                                var21 = this.getParametersNumber(var10);
                                if (var21 < 1) {
                                    var3 = false;
                                    this.errorMessage = this.errorMessage + var1 + var12 + "At least one argument was expected.\n";
                                }

                                if (var11.tokenId == 1 && (var21 % 2 != 0 || var21 < 2)) {
                                    var3 = false;
                                    this.errorMessage = this.errorMessage + var1 + var12 + "Expecting parity number of arguments.\n";
                                }
                            }

                            if (var11.tokenTypeId == 20 && var11.tokenId == 2 && var8.size() > 0 && var11.tokenLevel == ((SyntaxStackElement)var8.lastElement()).tokenLevel) {
                                var8.pop();
                            }
                        }
                    } catch (Exception var17) {
                        var3 = false;
                        this.errorMessage = this.errorMessage + var1 + "lexical error \n\n" + var17.getMessage() + "\n";
                    }

                    if (var3) {
                        this.errorMessage = this.errorMessage + var1 + "no errors.\n";
                        this.expressionWasModified = false;
                    } else {
                        this.errorMessage = this.errorMessage + var1 + "errors were found.\n";
                        this.expressionWasModified = true;
                    }

                    this.syntaxStatus = var3;
                    this.recursionCallPending = false;
                    return var3;
                }
            }
        }
    }

    public double calculate() {
        this.computingTime = 0.0D;
        long var1 = System.currentTimeMillis();
        if (this.verboseMode) {
            this.printSystemInfo("\n", false);
            this.printSystemInfo("\n", true);
            this.printSystemInfo("Starting ...\n", true);
            this.showArguments();
        }

        if (this.expressionWasModified || !this.syntaxStatus) {
            this.syntaxStatus = this.checkSyntax();
        }

        if (!this.syntaxStatus) {
            this.errorMessage = this.errorMessage + "Problem with expression syntax\n";
            if (this.verboseMode) {
                this.printSystemInfo("syntaxStatus == SYNTAX_ERROR_OR_STATUS_UNKNOWN, returning Double.NaN\n", false);
            }

            this.recursionCallsCounter = 0;
            return 0.0D / 0.0;
        } else {
            if (this.recursionCallsCounter == 0 || this.internalClone) {
                this.copyInitialTokens();
            }

            if (this.tokensList.size() == 0) {
                this.errorMessage = this.errorMessage + "Empty expression\n";
                if (this.verboseMode) {
                    this.printSystemInfo("tokensList.size() == 0, returning Double.NaN\n", false);
                }

                this.recursionCallsCounter = 0;
                return 0.0D / 0.0;
            } else if (this.recursionCallsCounter >= mXparser.MAX_RECURSION_CALLS) {
                this.errorMessage = this.errorMessage + "recursionCallsCounter >= MAX_RECURSION_CALLS\n";
                if (this.verboseMode) {
                    this.printSystemInfo("recursionCallsCounter >= mXparser.MAX_RECURSION_CALLS, returning Double.NaN\n", false);
                    this.printSystemInfo("recursionCallsCounter = " + this.recursionCallsCounter + "\n", false);
                    this.printSystemInfo("mXparser.MAX_RECURSION_CALLS = " + mXparser.MAX_RECURSION_CALLS + "\n", false);
                }

                this.recursionCallsCounter = 0;
                this.errorMessage = this.errorMessage + "\n[" + this.description + "][" + this.expressionString + "] Maximum recursion calls reached.\n";
                return 0.0D / 0.0;
            } else {
                ++this.recursionCallsCounter;
                ArrayList var50 = null;
                int var51 = 0;
                if (this.verboseMode) {
                    this.printSystemInfo("Starting calculation loop\n", true);
                }

                do {
                    if (mXparser.isCurrentCalculationCancelled()) {
                        this.errorMessage = this.errorMessage + "\nCancel request - finishing";
                        return 0.0D / 0.0;
                    }

                    int var43 = this.tokensList.size();
                    int var44 = -1;
                    int var45 = -1;
                    boolean var46 = true;
                    int var3 = -1;
                    int var4 = -1;
                    int var5 = -1;
                    int var6 = -1;
                    int var8 = -1;
                    int var7 = -1;
                    int var9 = -1;
                    int var10 = -1;
                    int var11 = -1;
                    int var12 = -1;
                    int var13 = -1;
                    int var14 = -1;
                    int var15 = -1;
                    int var16 = -1;
                    int var17 = -1;
                    int var18 = -1;
                    int var20 = -1;
                    int var21 = -1;
                    int var22 = -1;
                    int var19 = 0;
                    int var23 = -1;
                    int var24 = -1;
                    int var25 = -1;
                    int var26 = -1;
                    int var27 = -1;
                    int var28 = -1;
                    int var29 = -1;
                    int var30 = -1;
                    int var31 = -1;
                    int var32 = -1;
                    int var33 = -1;
                    int var34 = -1;
                    int var35 = -1;
                    int var36 = -1;
                    int var37 = -1;
                    int var38 = -1;
                    int var49 = -1;

                    Token var39;
                    do {
                        ++var49;
                        var39 = (Token)this.tokensList.get(var49);
                        if (var39.tokenTypeId == 8) {
                            var3 = var49;
                        } else if (var39.tokenTypeId == 6 && var39.tokenId == 1) {
                            var4 = var49;
                        } else if (var39.tokenTypeId == 7 && var39.tokenId == 1) {
                            var5 = var49;
                        }
                    } while(var49 < var43 - 1 && var3 < 0 && var4 < 0 && var5 < 0);

                    if (var3 < 0 && var4 < 0 && var5 < 0) {
                        Argument var42;
                        int var47;
                        for(var47 = 0; var47 < var43; ++var47) {
                            var39 = (Token)this.tokensList.get(var47);
                            if (var39.tokenLevel > var44) {
                                var44 = ((Token)this.tokensList.get(var47)).tokenLevel;
                                var45 = var47;
                            }

                            if (var39.tokenTypeId == 101) {
                                var42 = (Argument)this.argumentsList.get(((Token)this.tokensList.get(var47)).tokenId);
                                if (var42.argumentType == 1) {
                                    this.FREE_ARGUMENT(var47);
                                } else {
                                    var7 = var47;
                                }
                            } else if (var39.tokenTypeId == 9) {
                                this.CONSTANT(var47);
                            } else if (var39.tokenTypeId == 12) {
                                this.UNIT(var47);
                            } else if (var39.tokenTypeId == 104) {
                                this.USER_CONSTANT(var47);
                            } else if (var39.tokenTypeId == 10) {
                                this.RANDOM_VARIABLE(var47);
                            }
                        }

                        if (var45 < 0) {
                            this.errorMessage = this.errorMessage + "\nInternal error / strange token level - finishing";
                            return 0.0D / 0.0;
                        }

                        boolean var52;
                        if (var7 >= 0) {
                            do {
                                var52 = false;
                                int var60 = this.tokensList.size();

                                for(var47 = 0; var47 < var60; ++var47) {
                                    var39 = (Token)this.tokensList.get(var47);
                                    if (var39.tokenTypeId == 101) {
                                        var42 = (Argument)this.argumentsList.get(((Token)this.tokensList.get(var47)).tokenId);
                                        if (var42.argumentType == 2) {
                                            this.DEPENDENT_ARGUMENT(var47);
                                            var52 = true;
                                            break;
                                        }
                                    }
                                }
                            } while(var52);
                        } else {
                            for(var47 = var45; var47 < var43 && var44 == ((Token)this.tokensList.get(var47)).tokenLevel; ++var47) {
                            }

                            int var58 = var47 - 1;
                            if (this.verboseMode) {
                                this.printSystemInfo("Parsing (" + var45 + ", " + var58 + ") ", true);
                                this.showParsing(var45, var58);
                            }

                            int var48 = var45;

                            label575:
                            while(true) {
                                if (var48 > var58) {
                                    if (var19 <= 1) {
                                        break;
                                    }

                                    var17 = -1;
                                    var49 = var58 + 1;

                                    while(true) {
                                        --var49;
                                        var39 = (Token)this.tokensList.get(var49);
                                        if (var39.tokenTypeId == 1 && var39.tokenId == 5) {
                                            var17 = var49;
                                        }

                                        if (var49 <= var45 || var17 != -1) {
                                            break label575;
                                        }
                                    }
                                }

                                var52 = false;
                                boolean var53 = false;
                                var39 = (Token)this.tokensList.get(var48);
                                if (var48 - 1 >= 0) {
                                    Token var40 = (Token)this.tokensList.get(var48 - 1);
                                    if (var40.tokenTypeId == 0) {
                                        var52 = true;
                                    }
                                }

                                if (var48 + 1 < var43) {
                                    Token var41 = (Token)this.tokensList.get(var48 + 1);
                                    if (var41.tokenTypeId == 0) {
                                        var53 = true;
                                    }
                                }

                                if (var39.tokenTypeId == 102 && var8 < 0) {
                                    var8 = var48;
                                } else if (var39.tokenTypeId == 7 && var6 < 0) {
                                    var6 = var48;
                                } else if (var39.tokenTypeId == 6 && var9 < 0) {
                                    var9 = var48;
                                } else if (var39.tokenTypeId == 5 && var10 < 0) {
                                    var10 = var48;
                                } else if (var39.tokenTypeId == 4 && var11 < 0) {
                                    var11 = var48;
                                } else if (var39.tokenTypeId == 103 && var12 < 0) {
                                    var12 = var48;
                                } else if (var39.tokenTypeId == 1) {
                                    if (var39.tokenId == 5 && var52 && var53) {
                                        var17 = var48;
                                        ++var19;
                                    } else if (var39.tokenId == 9 && var52 && var53) {
                                        var18 = var48;
                                    } else if (var39.tokenId == 6 && var20 < 0 && var52) {
                                        var20 = var48;
                                    } else if (var39.tokenId == 8 && var22 < 0 && var52) {
                                        var22 = var48;
                                    } else if (var39.tokenId == 7 && var21 < 0 && var52 && var53) {
                                        var21 = var48;
                                    } else if (var39.tokenId == 1 && var13 < 0 && var53) {
                                        var13 = var48;
                                    } else if (var39.tokenId == 2 && var14 < 0 && var53) {
                                        var14 = var48;
                                    } else if (var39.tokenId == 3 && var15 < 0 && var52 && var53) {
                                        var15 = var48;
                                    } else if (var39.tokenId == 4 && var16 < 0 && var52 && var53) {
                                        var16 = var48;
                                    }
                                } else if (var39.tokenTypeId != 2) {
                                    if (var39.tokenTypeId == 3) {
                                        if (var39.tokenId == 1 && var28 < 0 && var52 && var53) {
                                            var28 = var48;
                                        } else if (var39.tokenId == 2 && var29 < 0 && var52 && var53) {
                                            var29 = var48;
                                        } else if (var39.tokenId == 3 && var30 < 0 && var52 && var53) {
                                            var30 = var48;
                                        } else if (var39.tokenId == 4 && var31 < 0 && var52 && var53) {
                                            var31 = var48;
                                        } else if (var39.tokenId == 5 && var32 < 0 && var52 && var53) {
                                            var32 = var48;
                                        } else if (var39.tokenId == 6 && var33 < 0 && var52 && var53) {
                                            var33 = var48;
                                        }
                                    } else if (var39.tokenTypeId == 11) {
                                        if (var39.tokenId == 1 && var38 < 0 && var53) {
                                            var38 = var48;
                                        } else if (var37 < 0 && var52 && var53) {
                                            var37 = var48;
                                        }
                                    } else if (var39.tokenTypeId == 20) {
                                        if (var39.tokenId == 3) {
                                            if (var34 < 0) {
                                                var50 = new ArrayList();
                                            }

                                            var50.add(var48);
                                            var34 = var48;
                                        } else if (var39.tokenId == 1 && var35 < 0) {
                                            var35 = var48;
                                        } else if (var39.tokenId == 2 && var36 < 0) {
                                            var36 = var48;
                                        }
                                    }
                                } else if (var39.tokenId == 11 && var23 < 0 && var53) {
                                    var23 = var48;
                                } else if (var52 && var53) {
                                    if ((var39.tokenId == 1 || var39.tokenId == 2) && var24 < 0) {
                                        var24 = var48;
                                    } else if ((var39.tokenId == 3 || var39.tokenId == 4 || var39.tokenId == 5) && var25 < 0) {
                                        var25 = var48;
                                    } else if ((var39.tokenId == 6 || var39.tokenId == 7 || var39.tokenId == 8 || var39.tokenId == 9 || var39.tokenId == 10) && var26 < 0) {
                                        var26 = var48;
                                    } else if (var27 < 0) {
                                        var27 = var48;
                                    }
                                }

                                ++var48;
                            }
                        }
                    }

                    if (var3 >= 0) {
                        this.calculusCalc(var3);
                    } else if (var4 >= 0) {
                        this.IF_CONDITION(var4);
                    } else if (var5 >= 0) {
                        this.IFF(var5);
                    } else if (var8 >= 0) {
                        this.RECURSIVE_ARGUMENT(var8);
                    } else if (var6 >= 0) {
                        this.variadicFunCalc(var6);
                    } else if (var9 >= 0) {
                        this.f3ArgCalc(var9);
                    } else if (var10 >= 0) {
                        this.f2ArgCalc(var10);
                    } else if (var11 >= 0) {
                        this.f1ArgCalc(var11);
                    } else if (var12 >= 0) {
                        this.USER_FUNCTION(var12);
                    } else if (var18 >= 0) {
                        this.TETRATION(var18);
                    } else if (var17 >= 0) {
                        this.POWER(var17);
                    } else if (var20 >= 0) {
                        this.FACT(var20);
                    } else if (var22 >= 0) {
                        this.PERC(var22);
                    } else if (var21 >= 0) {
                        this.MODULO(var21);
                    } else if (var23 >= 0) {
                        this.NEG(var23);
                    } else if (var38 >= 0) {
                        this.BITWISE_COMPL(var38);
                    } else if (var15 < 0 && var16 < 0) {
                        if (var14 < 0 && var13 < 0) {
                            if (var29 >= 0) {
                                this.NEQ(var29);
                            } else if (var28 >= 0) {
                                this.EQ(var28);
                            } else if (var30 >= 0) {
                                this.LT(var30);
                            } else if (var31 >= 0) {
                                this.GT(var31);
                            } else if (var32 >= 0) {
                                this.LEQ(var32);
                            } else if (var33 >= 0) {
                                this.GEQ(var33);
                            } else if (var34 >= 0) {
                                for(int var59 = var50.size() - 1; var59 >= 0; --var59) {
                                    this.COMMA((Integer)var50.get(var59));
                                }
                            } else if (var24 >= 0) {
                                this.bolCalc(var24);
                            } else if (var25 >= 0) {
                                this.bolCalc(var25);
                            } else if (var26 >= 0) {
                                this.bolCalc(var26);
                            } else if (var27 >= 0) {
                                this.bolCalc(var27);
                            } else if (var37 >= 0) {
                                this.bitwiseCalc(var37);
                            } else if (var35 >= 0 && var36 > var35) {
                                this.PARENTHESES(var35, var36);
                            } else if (this.tokensList.size() > 1) {
                                this.errorMessage = this.errorMessage + "\n[" + this.description + "][" + this.expressionString + "] Fatal error - not know what to do with tokens while calculate().\n";
                            }
                        } else if (var14 >= 0 && var13 >= 0) {
                            if (var14 <= var13) {
                                this.MINUS(var14);
                            } else {
                                this.PLUS(var13);
                            }
                        } else if (var14 >= 0) {
                            this.MINUS(var14);
                        } else {
                            this.PLUS(var13);
                        }
                    } else if (var15 >= 0 && var16 >= 0) {
                        if (var15 <= var16) {
                            this.MULTIPLY(var15);
                        } else {
                            this.DIVIDE(var16);
                        }
                    } else if (var15 >= 0) {
                        this.MULTIPLY(var15);
                    } else {
                        this.DIVIDE(var16);
                    }

                    if (this.verboseMode) {
                        this.showParsing(0, this.tokensList.size() - 1);
                        this.printSystemInfo(" done\n", false);
                    }

                    if (this.tokensList.size() == var43) {
                        ++var51;
                    } else {
                        var51 = 0;
                    }

                    if (var51 > 10) {
                        this.errorMessage = this.errorMessage + "\nInternal error, do not know what to do with the token, probably mXparser bug, please report - finishing";
                        return 0.0D / 0.0;
                    }
                } while(this.tokensList.size() > 1);

                if (this.verboseMode) {
                    this.printSystemInfo("Calculated value: " + ((Token)this.tokensList.get(0)).tokenValue + "\n", true);
                    this.printSystemInfo("Exiting\n", true);
                    this.printSystemInfo("\n", false);
                }

                long var61 = System.currentTimeMillis();
                this.computingTime = (double)(var61 - var1) / 1000.0D;
                this.recursionCallsCounter = 0;
                double var54 = ((Token)this.tokensList.get(0)).tokenValue;
                if (mXparser.almostIntRounding) {
                    double var56 = (double)Math.round(var54);
                    if (Math.abs(var54 - var56) <= BinaryRelations.getEpsilon()) {
                        var54 = var56;
                    }
                }

                return var54;
            }
        }
    }

    private void f1ArgCalc(int var1) {
        switch(((Token)this.tokensList.get(var1)).tokenId) {
            case 1:
                this.SIN(var1);
                break;
            case 2:
                this.COS(var1);
                break;
            case 3:
                this.TAN(var1);
                break;
            case 4:
                this.CTAN(var1);
                break;
            case 5:
                this.SEC(var1);
                break;
            case 6:
                this.COSEC(var1);
                break;
            case 7:
                this.ASIN(var1);
                break;
            case 8:
                this.ACOS(var1);
                break;
            case 9:
                this.ATAN(var1);
                break;
            case 10:
                this.ACTAN(var1);
                break;
            case 11:
                this.LN(var1);
                break;
            case 12:
                this.LOG2(var1);
                break;
            case 13:
                this.LOG10(var1);
                break;
            case 14:
                this.RAD(var1);
                break;
            case 15:
                this.EXP(var1);
                break;
            case 16:
                this.SQRT(var1);
                break;
            case 17:
                this.SINH(var1);
                break;
            case 18:
                this.COSH(var1);
                break;
            case 19:
                this.TANH(var1);
                break;
            case 20:
                this.COTH(var1);
                break;
            case 21:
                this.SECH(var1);
                break;
            case 22:
                this.CSCH(var1);
                break;
            case 23:
                this.DEG(var1);
                break;
            case 24:
                this.ABS(var1);
                break;
            case 25:
                this.SGN(var1);
                break;
            case 26:
                this.FLOOR(var1);
                break;
            case 27:
                this.CEIL(var1);
            case 28:
            default:
                break;
            case 29:
                this.NOT(var1);
                break;
            case 30:
                this.ARSINH(var1);
                break;
            case 31:
                this.ARCOSH(var1);
                break;
            case 32:
                this.ARTANH(var1);
                break;
            case 33:
                this.ARCOTH(var1);
                break;
            case 34:
                this.ARSECH(var1);
                break;
            case 35:
                this.ARCSCH(var1);
                break;
            case 36:
                this.SA(var1);
                break;
            case 37:
                this.SINC(var1);
                break;
            case 38:
                this.BELL_NUMBER(var1);
                break;
            case 39:
                this.LUCAS_NUMBER(var1);
                break;
            case 40:
                this.FIBONACCI_NUMBER(var1);
                break;
            case 41:
                this.HARMONIC_NUMBER(var1);
                break;
            case 42:
                this.IS_PRIME(var1);
                break;
            case 43:
                this.PRIME_COUNT(var1);
                break;
            case 44:
                this.EXP_INT(var1);
                break;
            case 45:
                this.LOG_INT(var1);
                break;
            case 46:
                this.OFF_LOG_INT(var1);
                break;
            case 47:
                this.GAUSS_ERF(var1);
                break;
            case 48:
                this.GAUSS_ERFC(var1);
                break;
            case 49:
                this.GAUSS_ERF_INV(var1);
                break;
            case 50:
                this.GAUSS_ERFC_INV(var1);
                break;
            case 51:
                this.ULP(var1);
                break;
            case 52:
                this.ISNAN(var1);
                break;
            case 53:
                this.NDIG10(var1);
                break;
            case 54:
                this.NFACT(var1);
                break;
            case 55:
                this.ARCSEC(var1);
                break;
            case 56:
                this.ARCCSC(var1);
                break;
            case 57:
                this.GAMMA(var1);
                break;
            case 58:
                this.LAMBERT_W0(var1);
                break;
            case 59:
                this.LAMBERT_W1(var1);
                break;
            case 60:
                this.SGN_GAMMA(var1);
                break;
            case 61:
                this.LOG_GAMMA(var1);
                break;
            case 62:
                this.DI_GAMMA(var1);
                break;
            case 63:
                this.UDF_PARAM(var1);
        }

    }

    private void f2ArgCalc(int var1) {
        switch(((Token)this.tokensList.get(var1)).tokenId) {
            case 1:
                this.LOG(var1);
                break;
            case 2:
                this.MOD(var1);
                break;
            case 3:
                this.BINOM_COEFF(var1);
                break;
            case 4:
                this.BERNOULLI_NUMBER(var1);
                break;
            case 5:
                this.STIRLING1_NUMBER(var1);
                break;
            case 6:
                this.STIRLING2_NUMBER(var1);
                break;
            case 7:
                this.WORPITZKY_NUMBER(var1);
                break;
            case 8:
                this.EULER_NUMBER(var1);
                break;
            case 9:
                this.KRONECKER_DELTA(var1);
                break;
            case 10:
                this.EULER_POLYNOMIAL(var1);
                break;
            case 11:
                this.HARMONIC2_NUMBER(var1);
                break;
            case 12:
                this.RND_VAR_UNIFORM_CONT(var1);
                break;
            case 13:
                this.RND_VAR_UNIFORM_DISCR(var1);
                break;
            case 14:
                this.ROUND(var1);
                break;
            case 15:
                this.RND_NORMAL(var1);
                break;
            case 16:
                this.NDIG(var1);
                break;
            case 17:
                this.DIGIT10(var1);
                break;
            case 18:
                this.FACTVAL(var1);
                break;
            case 19:
                this.FACTEXP(var1);
                break;
            case 20:
                this.ROOT(var1);
                break;
            case 21:
                this.INC_GAMMA_LOWER(var1);
                break;
            case 22:
                this.INC_GAMMA_UPPER(var1);
                break;
            case 23:
                this.REG_GAMMA_LOWER(var1);
                break;
            case 24:
                this.REG_GAMMA_UPPER(var1);
                break;
            case 25:
                this.PERMUTATIONS(var1);
                break;
            case 26:
                this.BETA(var1);
                break;
            case 27:
                this.LOG_BETA(var1);
        }

    }

    private void f3ArgCalc(int var1) {
        switch(((Token)this.tokensList.get(var1)).tokenId) {
            case 2:
                this.IF(var1);
                break;
            case 3:
                this.CHI(var1);
                break;
            case 4:
                this.CHI_LR(var1);
                break;
            case 5:
                this.CHI_L(var1);
                break;
            case 6:
                this.CHI_R(var1);
                break;
            case 7:
                this.PDF_UNIFORM_CONT(var1);
                break;
            case 8:
                this.CDF_UNIFORM_CONT(var1);
                break;
            case 9:
                this.QNT_UNIFORM_CONT(var1);
                break;
            case 10:
                this.PDF_NORMAL(var1);
                break;
            case 11:
                this.CDF_NORMAL(var1);
                break;
            case 12:
                this.QNT_NORMAL(var1);
                break;
            case 13:
                this.DIGIT(var1);
                break;
            case 14:
                this.INC_BETA(var1);
                break;
            case 15:
                this.REG_BETA(var1);
        }

    }

    private void variadicFunCalc(int var1) {
        switch(((Token)this.tokensList.get(var1)).tokenId) {
            case 1:
                this.IFF(var1);
                break;
            case 2:
                this.MIN_VARIADIC(var1);
                break;
            case 3:
                this.MAX_VARIADIC(var1);
                break;
            case 4:
                this.CONTINUED_FRACTION(var1);
                break;
            case 5:
                this.CONTINUED_POLYNOMIAL(var1);
                break;
            case 6:
                this.GCD(var1);
                break;
            case 7:
                this.LCM(var1);
                break;
            case 8:
                this.SUM_VARIADIC(var1);
                break;
            case 9:
                this.PROD_VARIADIC(var1);
                break;
            case 10:
                this.AVG_VARIADIC(var1);
                break;
            case 11:
                this.VAR_VARIADIC(var1);
                break;
            case 12:
                this.STD_VARIADIC(var1);
                break;
            case 13:
                this.RND_LIST(var1);
                break;
            case 14:
                this.COALESCE(var1);
                break;
            case 15:
                this.OR_VARIADIC(var1);
                break;
            case 16:
                this.AND_VARIADIC(var1);
                break;
            case 17:
                this.XOR_VARIADIC(var1);
                break;
            case 18:
                this.ARGMIN_VARIADIC(var1);
                break;
            case 19:
                this.ARGMAX_VARIADIC(var1);
                break;
            case 20:
                this.MEDIAN_VARIADIC(var1);
                break;
            case 21:
                this.MODE_VARIADIC(var1);
                break;
            case 22:
                this.BASE_VARIADIC(var1);
                break;
            case 23:
                this.NDIST_VARIADIC(var1);
        }

    }

    private void calculusCalc(int var1) {
        switch(((Token)this.tokensList.get(var1)).tokenId) {
            case 1:
                this.SUM(var1);
            case 2:
            case 4:
            default:
                break;
            case 3:
                this.PROD(var1);
                break;
            case 5:
                this.INTEGRAL(var1);
                break;
            case 6:
                this.DERIVATIVE(var1, 3);
                break;
            case 7:
                this.DERIVATIVE(var1, 1);
                break;
            case 8:
                this.DERIVATIVE(var1, 2);
                break;
            case 9:
                this.DERIVATIVE_NTH(var1, 3);
                break;
            case 10:
                this.FORWARD_DIFFERENCE(var1);
                break;
            case 11:
                this.BACKWARD_DIFFERENCE(var1);
                break;
            case 12:
                this.AVG(var1);
                break;
            case 13:
                this.VAR(var1);
                break;
            case 14:
                this.STD(var1);
                break;
            case 15:
                this.MIN(var1);
                break;
            case 16:
                this.MAX(var1);
                break;
            case 17:
                this.SOLVE(var1);
        }

    }

    private void bolCalc(int var1) {
        switch(((Token)this.tokensList.get(var1)).tokenId) {
            case 1:
                this.AND(var1);
                break;
            case 2:
                this.NAND(var1);
                break;
            case 3:
                this.OR(var1);
                break;
            case 4:
                this.NOR(var1);
                break;
            case 5:
                this.XOR(var1);
                break;
            case 6:
                this.IMP(var1);
                break;
            case 7:
                this.CIMP(var1);
                break;
            case 8:
                this.NIMP(var1);
                break;
            case 9:
                this.CNIMP(var1);
                break;
            case 10:
                this.EQV(var1);
        }

    }

    private void bitwiseCalc(int var1) {
        switch(((Token)this.tokensList.get(var1)).tokenId) {
            case 2:
                this.BITWISE_AND(var1);
                break;
            case 3:
                this.BITWISE_XOR(var1);
                break;
            case 4:
                this.BITWISE_OR(var1);
                break;
            case 5:
                this.BITWISE_LEFT_SHIFT(var1);
                break;
            case 6:
                this.BITWISE_RIGHT_SHIFT(var1);
        }

    }

    private void addUDFSpecificParserKeyWords() {
        this.addKeyWord("par", "Automatically generated function for user defined functions, returns function parameter value at index 'i'", 63, "par(i)", "4.2", 4);
        this.addKeyWord("[npar]", "Automatically generated constant for user defined functions, returns number of given function parameters", 303, "[npar]", "4.2", 9);
    }

    private void addParserKeyWords() {
        this.addKeyWord("+", "Addition", 1, "a + b", "1.0", 1);
        this.addKeyWord("-", "Subtraction", 2, "a - b", "1.0", 1);
        this.addKeyWord("*", "Nultiplication", 3, "a * b", "1.0", 1);
        this.addKeyWord("/", "Division", 4, "a / b", "1.0", 1);
        this.addKeyWord("^", "Exponentiation", 5, "a^b", "1.0", 1);
        this.addKeyWord("!", "Factorial", 6, "n!", "1.0", 1);
        this.addKeyWord("#", "Modulo function", 7, "a # b", "1.0", 1);
        this.addKeyWord("%", "Percentage", 8, "n%", "4.1", 1);
        this.addKeyWord("^^", "Tetration (hyper-4, power tower, exponential tower)", 9, "a^^n", "4.2", 1);
        this.addKeyWord("~", "Negation", 11, "~p", "1.0", 2);
        this.addKeyWord("&", "Logical conjunction (AND)", 1, "p & q", "1.0", 2);
        this.addKeyWord("&&", "Logical conjunction (AND)", 1, "p && q", "1.0", 2);
        this.addKeyWord("/\\", "Logical conjunction (AND)", 1, "p /\\ q", "1.0", 2);
        this.addKeyWord("~&", "NAND - Sheffer stroke", 2, "p ~& q", "1.0", 2);
        this.addKeyWord("~&&", "NAND - Sheffer stroke", 2, "p ~&& q", "1.0", 2);
        this.addKeyWord("~/\\", "NAND - Sheffer stroke", 2, "p ~/\\ q", "1.0", 2);
        this.addKeyWord("|", "Logical disjunction (OR)", 3, "p | q", "1.0", 2);
        this.addKeyWord("||", "Logical disjunction (OR)", 3, "p || q", "1.0", 2);
        this.addKeyWord("\\/", "Logical disjunction (OR)", 3, "p \\/ q", "1.0", 2);
        this.addKeyWord("~|", "Logical NOR", 4, "p ~| q", "1.0", 2);
        this.addKeyWord("~||", "Logical NOR", 4, "p ~|| q", "1.0", 2);
        this.addKeyWord("~\\/", "Logical NOR", 4, "p ~\\/ q", "1.0", 2);
        this.addKeyWord("(+)", "Exclusive or (XOR)", 5, "p (+) q", "1.0", 2);
        this.addKeyWord("-->", "Implication (IMP)", 6, "p --> q", "1.0", 2);
        this.addKeyWord("-/>", "Material nonimplication (NIMP)", 8, "p  -/> q", "1.0", 2);
        this.addKeyWord("<--", "Converse implication (CIMP)", 7, "p <-- q", "1.0", 2);
        this.addKeyWord("</-", "Converse nonimplication (CNIMP)", 9, "p </- q", "1.0", 2);
        this.addKeyWord("<->", "Logical biconditional (EQV)", 10, "p <-> q", "1.0", 2);
        this.addKeyWord("=", "Equality", 1, "a = b", "1.0", 3);
        this.addKeyWord("==", "Equality", 1, "a == b", "1.0", 3);
        this.addKeyWord("<>", "Inequation", 2, "a <> b", "1.0", 3);
        this.addKeyWord("~=", "Inequation", 2, "a ~= b", "1.0", 3);
        this.addKeyWord("!=", "Inequation", 2, "a != b", "1.0", 3);
        this.addKeyWord("<", "Lower than", 3, "a < b", "1.0", 3);
        this.addKeyWord(">", "Greater than", 4, "a > b", "1.0", 3);
        this.addKeyWord("<=", "Lower or equal", 5, "a <= b", "1.0", 3);
        this.addKeyWord(">=", "Greater or equal", 6, "a >= b", "1.0", 3);
        if (!this.parserKeyWordsOnly) {
            this.addKeyWord("sin", "Trigonometric sine function", 1, "sin(x)", "1.0", 4);
            this.addKeyWord("cos", "Trigonometric cosine function", 2, "cos(x)", "1.0", 4);
            this.addKeyWord("tan", "Trigonometric tangent function", 3, "tan(x)", "1.0", 4);
            this.addKeyWord("tg", "Trigonometric tangent function", 3, "tg(x)", "1.0", 4);
            this.addKeyWord("ctan", "Trigonometric cotangent function", 4, "ctan(x)", "1.0", 4);
            this.addKeyWord("ctg", "Trigonometric cotangent function", 4, "ctg(x)", "1.0", 4);
            this.addKeyWord("cot", "Trigonometric cotangent function", 4, "cot(x)", "1.0", 4);
            this.addKeyWord("sec", "Trigonometric secant function", 5, "sec(x)", "1.0", 4);
            this.addKeyWord("cosec", "Trigonometric cosecant function", 6, "cosec(x)", "1.0", 4);
            this.addKeyWord("csc", "Trigonometric cosecant function", 6, "csc(x)", "1.0", 4);
            this.addKeyWord("asin", "Inverse trigonometric sine function", 7, "asin(x)", "1.0", 4);
            this.addKeyWord("arsin", "Inverse trigonometric sine function", 7, "arsin(x)", "1.0", 4);
            this.addKeyWord("arcsin", "Inverse trigonometric sine function", 7, "arcsin(x)", "1.0", 4);
            this.addKeyWord("acos", "Inverse trigonometric cosine function", 8, "acos(x)", "1.0", 4);
            this.addKeyWord("arcos", "Inverse trigonometric cosine function", 8, "arcos(x)", "1.0", 4);
            this.addKeyWord("arccos", "Inverse trigonometric cosine function", 8, "arccos(x)", "1.0", 4);
            this.addKeyWord("atan", "Inverse trigonometric tangent function", 9, "atan(x)", "1.0", 4);
            this.addKeyWord("arctan", "Inverse trigonometric tangent function", 9, "arctan(x)", "1.0", 4);
            this.addKeyWord("atg", "Inverse trigonometric tangent function", 9, "atg(x)", "1.0", 4);
            this.addKeyWord("arctg", "Inverse trigonometric tangent function", 9, "arctg(x)", "1.0", 4);
            this.addKeyWord("actan", "Inverse trigonometric cotangent function", 10, "actan(x)", "1.0", 4);
            this.addKeyWord("arcctan", "Inverse trigonometric cotangent function", 10, "arcctan(x)", "1.0", 4);
            this.addKeyWord("actg", "Inverse trigonometric cotangent function", 10, "actg(x)", "1.0", 4);
            this.addKeyWord("arcctg", "Inverse trigonometric cotangent function", 10, "arcctg(x)", "1.0", 4);
            this.addKeyWord("acot", "Inverse trigonometric cotangent function", 10, "acot(x)", "1.0", 4);
            this.addKeyWord("arccot", "Inverse trigonometric cotangent function", 10, "arccot(x)", "1.0", 4);
            this.addKeyWord("ln", "Natural logarithm function (base e)", 11, "ln(x)", "1.0", 4);
            this.addKeyWord("log2", "Binary logarithm function (base 2)", 12, "log2(x)", "1.0", 4);
            this.addKeyWord("log10", "Common logarithm function (base 10)", 13, "log10(x)", "1.0", 4);
            this.addKeyWord("rad", "Degrees to radians function", 14, "rad(x)", "1.0", 4);
            this.addKeyWord("exp", "Exponential function", 15, "exp(x)", "1.0", 4);
            this.addKeyWord("sqrt", "Squre root function", 16, "sqrt(x)", "1.0", 4);
            this.addKeyWord("sinh", "Hyperbolic sine function", 17, "sinh(x)", "1.0", 4);
            this.addKeyWord("cosh", "Hyperbolic cosine function", 18, "cosh(x)", "1.0", 4);
            this.addKeyWord("tanh", "Hyperbolic tangent function", 19, "tanh(x)", "1.0", 4);
            this.addKeyWord("tgh", "Hyperbolic tangent function", 19, "tgh(x)", "1.0", 4);
            this.addKeyWord("ctanh", "Hyperbolic cotangent function", 20, "ctanh(x)", "1.0", 4);
            this.addKeyWord("coth", "Hyperbolic cotangent function", 20, "coth(x)", "1.0", 4);
            this.addKeyWord("ctgh", "Hyperbolic cotangent function", 20, "ctgh(x)", "1.0", 4);
            this.addKeyWord("sech", "Hyperbolic secant function", 21, "sech(x)", "1.0", 4);
            this.addKeyWord("csch", "Hyperbolic cosecant function", 22, "csch(x)", "1.0", 4);
            this.addKeyWord("cosech", "Hyperbolic cosecant function", 22, "cosech(x)", "1.0", 4);
            this.addKeyWord("deg", "Radians to degrees function", 23, "deg(x)", "1.0", 4);
            this.addKeyWord("abs", "Absolut value function", 24, "abs(x)", "1.0", 4);
            this.addKeyWord("sgn", "Signum function", 25, "sgn(x)", "1.0", 4);
            this.addKeyWord("floor", "Floor function", 26, "floor(x)", "1.0", 4);
            this.addKeyWord("ceil", "Ceiling function", 27, "ceil(x)", "1.0", 4);
            this.addKeyWord("not", "Negation function", 29, "not(x)", "1.0", 4);
            this.addKeyWord("asinh", "Inverse hyperbolic sine function", 30, "asinh(x)", "1.0", 4);
            this.addKeyWord("arsinh", "Inverse hyperbolic sine function", 30, "arsinh(x)", "1.0", 4);
            this.addKeyWord("arcsinh", "Inverse hyperbolic sine function", 30, "arcsinh(x)", "1.0", 4);
            this.addKeyWord("acosh", "Inverse hyperbolic cosine function", 31, "acosh(x)", "1.0", 4);
            this.addKeyWord("arcosh", "Inverse hyperbolic cosine function", 31, "arcosh(x)", "1.0", 4);
            this.addKeyWord("arccosh", "Inverse hyperbolic cosine function", 31, "arccosh(x)", "1.0", 4);
            this.addKeyWord("atanh", "Inverse hyperbolic tangent function", 32, "atanh(x)", "1.0", 4);
            this.addKeyWord("arctanh", "Inverse hyperbolic tangent function", 32, "arctanh(x)", "1.0", 4);
            this.addKeyWord("atgh", "Inverse hyperbolic tangent function", 32, "atgh(x)", "1.0", 4);
            this.addKeyWord("arctgh", "Inverse hyperbolic tangent function", 32, "arctgh(x)", "1.0", 4);
            this.addKeyWord("actanh", "Inverse hyperbolic cotangent function", 33, "actanh(x)", "1.0", 4);
            this.addKeyWord("arcctanh", "Inverse hyperbolic cotangent function", 33, "arcctanh(x)", "1.0", 4);
            this.addKeyWord("acoth", "Inverse hyperbolic cotangent function", 33, "acoth(x)", "1.0", 4);
            this.addKeyWord("arcoth", "Inverse hyperbolic cotangent function", 33, "arcoth(x)", "1.0", 4);
            this.addKeyWord("arccoth", "Inverse hyperbolic cotangent function", 33, "arccoth(x)", "1.0", 4);
            this.addKeyWord("actgh", "Inverse hyperbolic cotangent function", 33, "actgh(x)", "1.0", 4);
            this.addKeyWord("arcctgh", "Inverse hyperbolic cotangent function", 33, "arcctgh(x)", "1.0", 4);
            this.addKeyWord("asech", "Inverse hyperbolic secant function", 34, "asech(x)", "1.0", 4);
            this.addKeyWord("arsech", "Inverse hyperbolic secant function", 34, "arsech(x)", "1.0", 4);
            this.addKeyWord("arcsech", "Inverse hyperbolic secant function", 34, "arcsech(x)", "1.0", 4);
            this.addKeyWord("acsch", "Inverse hyperbolic cosecant function", 35, "acsch(x)", "1.0", 4);
            this.addKeyWord("arcsch", "Inverse hyperbolic cosecant function", 35, "arcsch(x)", "1.0", 4);
            this.addKeyWord("arccsch", "Inverse hyperbolic cosecant function", 35, "arccsch(x)", "1.0", 4);
            this.addKeyWord("acosech", "Inverse hyperbolic cosecant function", 35, "acosech(x)", "1.0", 4);
            this.addKeyWord("arcosech", "Inverse hyperbolic cosecant function", 35, "arcosech(x)", "1.0", 4);
            this.addKeyWord("arccosech", "Inverse hyperbolic cosecant function", 35, "arccosech(x)", "1.0", 4);
            this.addKeyWord("sinc", "Sinc function (normalized)", 36, "sinc(x)", "1.0", 4);
            this.addKeyWord("Sa", "Sinc function (normalized)", 36, "Sa(x)", "1.0", 4);
            this.addKeyWord("Sinc", "Sinc function (unnormalized)", 37, "Sinc(x)", "1.0", 4);
            this.addKeyWord("Bell", "Bell number", 38, "Bell(n)", "1.0", 4);
            this.addKeyWord("Fib", "Fibonacci number", 40, "Fib(n)", "1.0", 4);
            this.addKeyWord("Luc", "Lucas number", 39, "Luc(n)", "1.0", 4);
            this.addKeyWord("harm", "Harmonic number", 41, "harm(n)", "1.0", 4);
            this.addKeyWord("ispr", "Prime number test (is number a prime?)", 42, "ispr(n)", "2.3", 4);
            this.addKeyWord("Pi", "Prime-counting function - Pi(x)", 43, "Pi(n)", "2.3", 4);
            this.addKeyWord("Ei", "Exponential integral function (non-elementary special function) - usage example: Ei(x)", 44, "Ei(x)", "2.3", 4);
            this.addKeyWord("li", "Logarithmic integral function (non-elementary special function) - usage example: li(x)", 45, "li(x)", "2.3", 4);
            this.addKeyWord("Li", "Offset logarithmic integral function (non-elementary special function) - usage example: Li(x)", 46, "Li(x)", "2.3", 4);
            this.addKeyWord("erf", "Gauss error function (non-elementary special function) - usage example: 2 + erf(x)", 47, "erf(x)", "3.0", 4);
            this.addKeyWord("erfc", "Gauss complementary error function (non-elementary special function) - usage example: 1 - erfc(x)", 48, "erfc(x)", "3.0", 4);
            this.addKeyWord("erfInv", "Inverse Gauss error function (non-elementary special function) - usage example: erfInv(x)", 49, "erfInv(x)", "3.0", 4);
            this.addKeyWord("erfcInv", "Inverse Gauss complementary error function (non-elementary special function) - usage example: erfcInv(x)", 50, "erfcInv(x)", "3.0", 4);
            this.addKeyWord("ulp", "Unit in The Last Place - ulp(0.1)", 51, "ulp(x)", "3.0", 4);
            this.addKeyWord("isNaN", "Returns true = 1 if value is a Not-a-Number (NaN), false = 0 otherwise - usage example: isNaN(x)", 52, "isNaN(x)", "4.1", 4);
            this.addKeyWord("ndig10", "Number of digits in numeral system with base 10", 53, "ndig10(x)", "4.1", 4);
            this.addKeyWord("nfact", "Prime decomposition - number of distinct prime factors", 54, "nfact(x)", "4.1", 4);
            this.addKeyWord("arcsec", "Inverse trigonometric secant", 55, "arcsec(x)", "4.1", 4);
            this.addKeyWord("arccsc", "Inverse trigonometric cosecant", 56, "arccsc(x)", "4.1", 4);
            this.addKeyWord("Gamma", "Gamma special function Γ(s)", 57, "Gamma(x)", "4.2", 4);
            this.addKeyWord("LambW0", "Lambert-W special function, principal branch 0, also called the omega function or product logarithm", 58, "LambW0(x)", "4.2", 4);
            this.addKeyWord("LambW1", "Lambert-W special function, branch -1, also called the omega function or product logarithm", 59, "LambW1(x)", "4.2", 4);
            this.addKeyWord("sgnGamma", "Signum of Gamma special function, Γ(s)", 60, "sgnGamma(x)", "4.2", 4);
            this.addKeyWord("logGamma", "Log Gamma special function, lnΓ(s)", 61, "logGamma(x)", "4.2", 4);
            this.addKeyWord("diGamma", "Digamma function as the logarithmic derivative of the Gamma special function, ψ(x)", 62, "diGamma(x)", "4.2", 4);
            this.addKeyWord("log", "Logarithm function", 1, "log(a, b)", "1.0", 5);
            this.addKeyWord("mod", "Modulo function", 2, "mod(a, b)", "1.0", 5);
            this.addKeyWord("C", "Binomial coefficient function, number of k-combinations that can be drawn from n-elements set", 3, "C(n, k)", "1.0", 5);
            this.addKeyWord("nCk", "Binomial coefficient function, number of k-combinations that can be drawn from n-elements set", 3, "nCk(n,k)", "4.2", 5);
            this.addKeyWord("Bern", "Bernoulli numbers", 4, "Bern(m, n)", "1.0", 5);
            this.addKeyWord("Stirl1", "Stirling numbers of the first kind", 5, "Stirl1(n, k)", "1.0", 5);
            this.addKeyWord("Stirl2", "Stirling numbers of the second kind", 6, "Stirl2(n, k)", "1.0", 5);
            this.addKeyWord("Worp", "Worpitzky number", 7, "Worp(n, k)", "1.0", 5);
            this.addKeyWord("Euler", "Euler number", 8, "Euler(n, k)", "1.0", 5);
            this.addKeyWord("KDelta", "Kronecker delta", 9, "KDelta(i, j)", "1.0", 5);
            this.addKeyWord("EulerPol", "EulerPol", 10, "EulerPol", "1.0", 5);
            this.addKeyWord("Harm", "Harmonic number", 11, "Harm(x, n)", "1.0", 5);
            this.addKeyWord("rUni", "Random variable - Uniform continuous distribution U(a,b), usage example: 2*rUni(2,10)", 12, "rUni(a, b)", "3.0", 5);
            this.addKeyWord("rUnid", "Random variable - Uniform discrete distribution U{a,b}, usage example: 2*rUnid(2,100)", 13, "rUnid(a, b)", "3.0", 5);
            this.addKeyWord("round", "Half-up rounding, usage examples: round(2.2, 0) = 2, round(2.6, 0) = 3, round(2.66,1) = 2.7", 14, "round(x, n)", "3.0", 5);
            this.addKeyWord("rNor", "Random variable - Normal distribution N(m,s) m - mean, s - stddev, usage example: 3*rNor(0,1)", 15, "rNor(mean, stdv)", "3.0", 5);
            this.addKeyWord("ndig", "Number of digits representing the number in numeral system with given base", 16, "ndig(number, base)", "4.1", 5);
            this.addKeyWord("dig10", "Digit at position 1 ... n (left -> right) or 0 ... -(n-1) (right -> left) - base 10 numeral system", 17, "dig10(num, pos)", "4.1", 5);
            this.addKeyWord("factval", "Prime decomposition - factor value at position between 1 ... nfact(n) - ascending order by factor value", 18, "factval(number, factorid)", "4.1", 5);
            this.addKeyWord("factexp", "Prime decomposition - factor exponent / multiplicity at position between 1 ... nfact(n) - ascending order by factor value", 19, "factexp(number, factorid)", "4.1", 5);
            this.addKeyWord("root", "N-th order root of a number", 20, "root(rootorder, number)", "4.1", 5);
            this.addKeyWord("GammaL", "Lower incomplete gamma special function, γ(s,x)", 21, "GammaL(s,x)", "4.2", 5);
            this.addKeyWord("GammaU", "Upper incomplete Gamma special function, Γ(s,x)", 22, "GammaU(s,x)", "4.2", 5);
            this.addKeyWord("GammaRegL", "Lower regularized P gamma special function, P(s,x)", 23, "GammaRegL(s,x)", "4.2", 5);
            this.addKeyWord("GammaRegU", "Upper regularized Q Gamma special function, Q(s,x)", 24, "GammaRegU(s,x)", "4.2", 5);
            this.addKeyWord("GammaP", "Lower regularized P gamma special function, P(s,x)", 23, "GammaP(s,x)", "4.2", 5);
            this.addKeyWord("GammaQ", "Upper regularized Q Gamma special function, Q(s,x)", 24, "GammaQ(s,x)", "4.2", 5);
            this.addKeyWord("nPk", "Number of k-permutations that can be drawn from n-elements set", 25, "nPk(n,k)", "4.2", 5);
            this.addKeyWord("Beta", "The Beta special function B(x,y), also called the Euler integral of the first kind", 26, "Beta(x,y)", "4.2", 5);
            this.addKeyWord("logBeta", "The Log Beta special function ln B(x,y), also called the Log Euler integral of the first kind, ln B(x,y)", 27, "logBeta(x,y)", "4.2", 5);
            this.addKeyWord("if", "If function", 1, "if( cond, expr-if-true, expr-if-false )", "1.0", 6);
            this.addKeyWord("chi", "Characteristic function for x in (a,b)", 3, "chi(x, a, b)", "1.0", 6);
            this.addKeyWord("CHi", "Characteristic function for x in [a,b]", 4, "CHi(x, a, b)", "1.0", 6);
            this.addKeyWord("Chi", "Characteristic function for x in [a,b)", 5, "Chi(x, a, b)", "1.0", 6);
            this.addKeyWord("cHi", "Characteristic function for x in (a,b]", 6, "cHi(x, a, b)", "1.0", 6);
            this.addKeyWord("pUni", "Probability distribution function - Uniform continuous distribution U(a,b)", 7, "pUni(x, a, b)", "3.0", 6);
            this.addKeyWord("cUni", "Cumulative distribution function - Uniform continuous distribution U(a,b)", 8, "cUni(x, a, b)", "3.0", 6);
            this.addKeyWord("qUni", "Quantile function (inverse cumulative distribution function) - Uniform continuous distribution U(a,b)", 9, "qUni(q, a, b)", "3.0", 6);
            this.addKeyWord("pNor", "Probability distribution function - Normal distribution N(m,s)", 10, "pNor(x, mean, stdv)", "3.0", 6);
            this.addKeyWord("cNor", "Cumulative distribution function - Normal distribution N(m,s)", 11, "cNor(x, mean, stdv)", "3.0", 6);
            this.addKeyWord("qNor", "Quantile function (inverse cumulative distribution function)", 12, "qNor(q, mean, stdv)", "3.0", 6);
            this.addKeyWord("dig", "Digit at position 1 ... n (left -> right) or 0 ... -(n-1) (right -> left) - numeral system with given base", 13, "dig(num, pos, base)", "4.1", 6);
            this.addKeyWord("BetaInc", "The incomplete beta special function B(x; a, b), also called the incomplete Euler integral of the first kind", 14, "BetaInc(x,a,b)", "4.2", 6);
            this.addKeyWord("BetaReg", "The regularized incomplete beta (or regularized beta) special function I(x; a, b), also called the regularized incomplete Euler integral of the first kind", 15, "BetaReg(x,a,b)", "4.2", 6);
            this.addKeyWord("BetaI", "The regularized incomplete beta (or regularized beta) special function I(x; a, b), also called the regularized incomplete Euler integral of the first kind", 15, "BetaI(x,a,b)", "4.2", 6);
            this.addKeyWord("iff", "If function", 1, "iff( cond-1, expr-1; ... ; cond-n, expr-n )", "1.0", 7);
            this.addKeyWord("min", "Minimum function", 2, "min(a1, ..., an)", "1.0", 7);
            this.addKeyWord("max", "Maximum function", 3, "max(a1, ..., an)", "1.0", 7);
            this.addKeyWord("ConFrac", "Continued fraction", 4, "ConFrac(a1, ..., an)", "1.0", 7);
            this.addKeyWord("ConPol", "Continued polynomial", 5, "ConPol(a1, ..., an)", "1.0", 7);
            this.addKeyWord("gcd", "Greatest common divisor", 6, "gcd(a1, ..., an)", "1.0", 7);
            this.addKeyWord("lcm", "Least common multiple", 7, "lcm(a1, ..., an)", "1.0", 7);
            this.addKeyWord("add", "Summation operator", 8, "add(a1, ..., an)", "2.4", 7);
            this.addKeyWord("multi", "Multiplication", 9, "multi(a1, ..., an)", "2.4", 7);
            this.addKeyWord("mean", "Mean / average value", 10, "mean(a1, ..., an)", "2.4", 7);
            this.addKeyWord("var", "Bias-corrected sample variance", 11, "var(a1, ..., an)", "2.4", 7);
            this.addKeyWord("std", "Bias-corrected sample standard deviation", 12, "std(a1, ..., an)", "2.4", 7);
            this.addKeyWord("rList", "Random number from given list of numbers", 13, "rList(a1, ..., an)", "3.0", 7);
            this.addKeyWord("coalesce", "Returns the first non-NaN value", 14, "coalesce(a1, ..., an)", "4.1", 7);
            this.addKeyWord("or", "Logical disjunction (OR) - variadic", 15, "or(a1, ..., an)", "4.1", 7);
            this.addKeyWord("and", "Logical conjunction (AND) - variadic", 16, "and(a1, ..., an)", "4.1", 7);
            this.addKeyWord("xor", "Exclusive or (XOR) - variadic", 17, "xor(a1, ..., an)", "4.1", 7);
            this.addKeyWord("argmin", "Arguments / indices of the minima", 18, "argmin(a1, ..., an)", "4.1", 7);
            this.addKeyWord("argmax", "Arguments / indices of the maxima", 19, "argmax(a1, ..., an)", "4.1", 7);
            this.addKeyWord("med", "The sample median", 20, "med(a1, ..., an)", "4.1", 7);
            this.addKeyWord("mode", "Mode - the value that appears most often", 21, "mode(a1, ..., an)", "4.1", 7);
            this.addKeyWord("base", "Returns number in given numeral system base represented by list of digits", 22, "base(b, d1, ..., dn)", "4.1", 7);
            this.addKeyWord("ndist", "Number of distinct values", 23, "ndist(v1, ..., vn)", "4.1", 7);
            this.addKeyWord("sum", "Summation operator - SIGMA", 1, "sum( i, from, to, expr , <by> )", "1.0", 8);
            this.addKeyWord("prod", "Product operator - PI", 3, "prod( i, from, to, expr , <by> )", "1.0", 8);
            this.addKeyWord("int", "Definite integral operator", 5, "int( expr, arg, from, to )", "1.0", 8);
            this.addKeyWord("der", "Derivative operator", 6, "der( expr, arg, <point> )", "1.0", 8);
            this.addKeyWord("der-", "Left derivative operator", 7, "der-( expr, arg, <point> )", "1.0", 8);
            this.addKeyWord("der+", "Right derivative operator", 8, "der+( expr, arg, <point> )", "1.0", 8);
            this.addKeyWord("dern", "n-th derivative operator", 9, "dern( expr, n, arg )", "1.0", 8);
            this.addKeyWord("diff", "Forward difference operator", 10, "diff( expr, arg, <delta> )", "1.0", 8);
            this.addKeyWord("difb", "Backward difference operator", 11, "difb( expr, arg, <delta> )", "1.0", 8);
            this.addKeyWord("avg", "Average operator", 12, "avg( i, from, to, expr , <by> )", "2.4", 8);
            this.addKeyWord("vari", "Bias-corrected sample variance operator", 13, "vari( i, from, to, expr , <by> )", "2.4", 8);
            this.addKeyWord("stdi", "Bias-corrected sample standard deviation operator", 14, "stdi( i, from, to, expr , <by> )", "2.4", 8);
            this.addKeyWord("mini", "Minimum value", 15, "mini( i, from, to, expr , <by> )", "2.4", 8);
            this.addKeyWord("maxi", "Maximum value", 16, "maxi( i, from, to, expr , <by> )", "2.4", 8);
            this.addKeyWord("solve", "f(x) = 0 equation solving, function root finding", 17, "solve( expr, arg, from, to )", "4.0", 8);
            this.addKeyWord("pi", "Pi, Archimedes' constant or Ludolph's number", 1, "pi", "1.0", 9);
            this.addKeyWord("e", "Napier's constant, or Euler's number, base of Natural logarithm", 2, "e", "1.0", 9);
            this.addKeyWord("[gam]", "Euler-Mascheroni constant", 3, "[gam]", "1.0", 9);
            this.addKeyWord("[phi]", "Golden ratio", 4, "[phi]", "1.0", 9);
            this.addKeyWord("[PN]", "Plastic constant", 5, "[PN]", "1.0", 9);
            this.addKeyWord("[B*]", "Embree-Trefethen constant", 6, "[B*]", "1.0", 9);
            this.addKeyWord("[F'd]", "Feigenbaum constant alfa", 7, "[F'd]", "1.0", 9);
            this.addKeyWord("[F'a]", "Feigenbaum constant delta", 8, "[F'a]", "1.0", 9);
            this.addKeyWord("[C2]", "Twin prime constant", 9, "[C2]", "1.0", 9);
            this.addKeyWord("[M1]", "Meissel-Mertens constant", 10, "[M1]", "1.0", 9);
            this.addKeyWord("[B2]", "Brun's constant for twin primes", 11, "[B2]", "1.0", 9);
            this.addKeyWord("[B4]", "Brun's constant for prime quadruplets", 12, "[B4]", "1.0", 9);
            this.addKeyWord("[BN'L]", "de Bruijn-Newman constant", 13, "[BN'L]", "1.0", 9);
            this.addKeyWord("[Kat]", "Catalan's constant", 14, "[Kat]", "1.0", 9);
            this.addKeyWord("[K*]", "Landau-Ramanujan constant", 15, "[K*]", "1.0", 9);
            this.addKeyWord("[K.]", "Viswanath's constant", 16, "[K.]", "1.0", 9);
            this.addKeyWord("[B'L]", "Legendre's constant", 17, "[B'L]", "1.0", 9);
            this.addKeyWord("[RS'm]", "Ramanujan-Soldner constant", 18, "[RS'm]", "1.0", 9);
            this.addKeyWord("[EB'e]", "Erdos-Borwein constant", 19, "[EB'e]", "1.0", 9);
            this.addKeyWord("[Bern]", "Bernstein's constant", 20, "[Bern]", "1.0", 9);
            this.addKeyWord("[GKW'l]", "Gauss-Kuzmin-Wirsing constant", 21, "[GKW'l]", "1.0", 9);
            this.addKeyWord("[HSM's]", "Hafner-Sarnak-McCurley constant", 22, "[HSM's]", "1.0", 9);
            this.addKeyWord("[lm]", "Golomb-Dickman constant", 23, "[lm]", "1.0", 9);
            this.addKeyWord("[Cah]", "Cahen's constant", 24, "[Cah]", "1.0", 9);
            this.addKeyWord("[Ll]", "Laplace limit", 25, "[Ll]", "1.0", 9);
            this.addKeyWord("[AG]", "Alladi-Grinstead constant", 26, "[AG]", "1.0", 9);
            this.addKeyWord("[L*]", "Lengyel's constant", 27, "[L*]", "1.0", 9);
            this.addKeyWord("[L.]", "Levy's constant", 28, "[L.]", "1.0", 9);
            this.addKeyWord("[Dz3]", "Apery's constant", 29, "[Dz3]", "1.0", 9);
            this.addKeyWord("[A3n]", "Mills' constant", 30, "[A3n]", "1.0", 9);
            this.addKeyWord("[Bh]", "Backhouse's constant", 31, "[Bh]", "1.0", 9);
            this.addKeyWord("[Pt]", "Porter's constant", 32, "[Pt]", "1.0", 9);
            this.addKeyWord("[L2]", "Lieb's square ice constant", 33, "[L2]", "1.0", 9);
            this.addKeyWord("[Nv]", "Niven's constant", 34, "[Nv]", "1.0", 9);
            this.addKeyWord("[Ks]", "Sierpinski's constant", 35, "[Ks]", "1.0", 9);
            this.addKeyWord("[Kh]", "Khinchin's constant", 36, "[Kh]", "1.0", 9);
            this.addKeyWord("[FR]", "Fransen-Robinson constant", 37, "[FR]", "1.0", 9);
            this.addKeyWord("[La]", "Landau's constant", 38, "[La]", "1.0", 9);
            this.addKeyWord("[P2]", "Parabolic constant", 39, "[P2]", "1.0", 9);
            this.addKeyWord("[Om]", "Omega constant", 40, "[Om]", "1.0", 9);
            this.addKeyWord("[MRB]", "MRB constant", 41, "[MRB]", "1.0", 9);
            this.addKeyWord("[li2]", "li(2) - Logarithmic integral function at x=2", 42, "[li2]", "2.3", 9);
            this.addKeyWord("[EG]", "Gompertz constant", 43, "[EG]", "2.3", 9);
            this.addKeyWord("[c]", "<Physical Constant> Light speed in vacuum [m/s] (m=1, s=1)", 101, "[c]", "4.0", 9);
            this.addKeyWord("[G.]", "<Physical Constant> Gravitational constant (m=1, kg=1, s=1)]", 102, "[G.]", "4.0", 9);
            this.addKeyWord("[g]", "<Physical Constant> Gravitational acceleration on Earth [m/s^2] (m=1, s=1)", 103, "[g]", "4.0", 9);
            this.addKeyWord("[hP]", "<Physical Constant> Planck constant (m=1, kg=1, s=1)", 104, "[hP]", "4.0", 9);
            this.addKeyWord("[h-]", "<Physical Constant> Reduced Planck constant / Dirac constant (m=1, kg=1, s=1)]", 105, "[h-]", "4.0", 9);
            this.addKeyWord("[lP]", "<Physical Constant> Planck length [m] (m=1)", 106, "[lP]", "4.0", 9);
            this.addKeyWord("[mP]", "<Physical Constant> Planck mass [kg] (kg=1)", 107, "[mP]", "4.0", 9);
            this.addKeyWord("[tP]", "<Physical Constant> Planck time [s] (s=1)", 108, "[tP]", "4.0", 9);
            this.addKeyWord("[ly]", "<Astronomical Constant> Light year [m] (m=1)", 201, "[ly]", "4.0", 9);
            this.addKeyWord("[au]", "<Astronomical Constant> Astronomical unit [m] (m=1)", 202, "[au]", "4.0", 9);
            this.addKeyWord("[pc]", "<Astronomical Constant> Parsec [m] (m=1)", 203, "[pc]", "4.0", 9);
            this.addKeyWord("[kpc]", "<Astronomical Constant> Kiloparsec [m] (m=1)", 204, "[kpc]", "4.0", 9);
            this.addKeyWord("[Earth-R-eq]", "<Astronomical Constant> Earth equatorial radius [m] (m=1)", 205, "[Earth-R-eq]", "4.0", 9);
            this.addKeyWord("[Earth-R-po]", "<Astronomical Constant> Earth polar radius [m] (m=1)", 206, "[Earth-R-po]", "4.0", 9);
            this.addKeyWord("[Earth-R]", "<Astronomical Constant> Earth mean radius (m=1)", 207, "[Earth-R]", "4.0", 9);
            this.addKeyWord("[Earth-M]", "<Astronomical Constant> Earth mass [kg] (kg=1)", 208, "[Earth-M]", "4.0", 9);
            this.addKeyWord("[Earth-D]", "<Astronomical Constant> Earth-Sun distance - semi major axis [m] (m=1)", 209, "[Earth-D]", "4.0", 9);
            this.addKeyWord("[Moon-R]", "<Astronomical Constant> Moon mean radius [m] (m=1)", 210, "[Moon-R]", "4.0", 9);
            this.addKeyWord("[Moon-M]", "<Astronomical Constant> Moon mass [kg] (kg=1)", 211, "[Moon-M]", "4.0", 9);
            this.addKeyWord("[Moon-D]", "<Astronomical Constant> Moon-Earth distance - semi major axis [m] (m=1)", 212, "[Moon-D]", "4.0", 9);
            this.addKeyWord("[Solar-R]", "<Astronomical Constant> Solar mean radius [m] (m=1)", 213, "[Solar-R]", "4.0", 9);
            this.addKeyWord("[Solar-M]", "<Astronomical Constant> Solar mass [kg] (kg=1)", 214, "[Solar-M]", "4.0", 9);
            this.addKeyWord("[Mercury-R]", "<Astronomical Constant> Mercury mean radius [m] (m=1)", 215, "[Mercury-R]", "4.0", 9);
            this.addKeyWord("[Mercury-M]", "<Astronomical Constant> Mercury mass [kg] (kg=1)", 216, "[Mercury-M]", "4.0", 9);
            this.addKeyWord("[Mercury-D]", "<Astronomical Constant> Mercury-Sun distance - semi major axis [m] (m=1)", 217, "[Mercury-D]", "4.0", 9);
            this.addKeyWord("[Venus-R]", "<Astronomical Constant> Venus mean radius [m] (m=1)", 218, "[Venus-R]", "4.0", 9);
            this.addKeyWord("[Venus-M]", "<Astronomical Constant> Venus mass [kg] (kg=1)", 219, "[Venus-M]", "4.0", 9);
            this.addKeyWord("[Venus-D]", "<Astronomical Constant> Venus-Sun distance - semi major axis [m] (m=1)", 220, "[Venus-D]", "4.0", 9);
            this.addKeyWord("[Mars-R]", "<Astronomical Constant> Mars mean radius [m] (m=1)", 221, "[Mars-R]", "4.0", 9);
            this.addKeyWord("[Mars-M]", "<Astronomical Constant> Mars mass [kg] (kg=1)", 222, "[Mars-M]", "4.0", 9);
            this.addKeyWord("[Mars-D]", "<Astronomical Constant> Mars-Sun distance - semi major axis [m] (m=1)", 223, "[Mars-D]", "4.0", 9);
            this.addKeyWord("[Jupiter-R]", "<Astronomical Constant> Jupiter mean radius [m] (m=1)", 224, "[Jupiter-R]", "4.0", 9);
            this.addKeyWord("[Jupiter-M]", "<Astronomical Constant> Jupiter mass [kg] (kg=1)", 225, "[Jupiter-M]", "4.0", 9);
            this.addKeyWord("[Jupiter-D]", "<Astronomical Constant> Jupiter-Sun distance - semi major axis [m] (m=1)", 226, "[Jupiter-D]", "4.0", 9);
            this.addKeyWord("[Saturn-R]", "<Astronomical Constant> Saturn mean radius [m] (m=1)", 227, "[Saturn-R]", "4.0", 9);
            this.addKeyWord("[Saturn-M]", "<Astronomical Constant> Saturn mass [kg] (kg=1)", 228, "[Saturn-M]", "4.0", 9);
            this.addKeyWord("[Saturn-D]", "<Astronomical Constant> Saturn-Sun distance - semi major axis [m] (m=1)", 229, "[Saturn-D]", "4.0", 9);
            this.addKeyWord("[Uranus-R]", "<Astronomical Constant> Uranus mean radius [m] (m=1)", 230, "[Uranus-R]", "4.0", 9);
            this.addKeyWord("[Uranus-M]", "<Astronomical Constant> Uranus mass [kg] (kg=1)", 231, "[Uranus-M]", "4.0", 9);
            this.addKeyWord("[Uranus-D]", "<Astronomical Constant> Uranus-Sun distance - semi major axis [m] (m=1)", 232, "[Uranus-D]", "4.0", 9);
            this.addKeyWord("[Neptune-R]", "<Astronomical Constant> Neptune mean radius [m] (m=1)", 233, "[Neptune-R]", "4.0", 9);
            this.addKeyWord("[Neptune-M]", "<Astronomical Constant> Neptune mass [kg] (kg=1)", 234, "[Neptune-M]", "4.0", 9);
            this.addKeyWord("[Neptune-D]", "<Astronomical Constant> Neptune-Sun distance - semi major axis [m] (m=1)", 235, "[Neptune-D]", "4.0", 9);
            this.addKeyWord("[true]", "Boolean True represented as double, [true] = 1", 301, "[true]", "4.1", 9);
            this.addKeyWord("[false]", "Boolean False represented as double, [false] = 0", 302, "[false]", "4.1", 9);
            this.addKeyWord("[NaN]", "Not-a-Number", 999, "[NaN]", "4.1", 9);
            this.addKeyWord("[Uni]", "Random variable - Uniform continuous distribution U(0,1)", 1, "[Uni]", "3.0", 10);
            this.addKeyWord("[Int]", "Random variable - random integer", 2, "[Int]", "3.0", 10);
            this.addKeyWord("[Int1]", "Random variable - random integer - Uniform discrete distribution U{-10^1, 10^1}", 3, "[Int1]", "3.0", 10);
            this.addKeyWord("[Int2]", "Random variable - random integer - Uniform discrete distribution U{-10^2, 10^2}", 4, "[Int2]", "3.0", 10);
            this.addKeyWord("[Int3]", "Random variable - random integer - Uniform discrete distribution U{-10^3, 10^3}", 5, "[Int3]", "3.0", 10);
            this.addKeyWord("[Int4]", "Random variable - random integer - Uniform discrete distribution U{-10^4, 10^4}", 6, "[Int4]", "3.0", 10);
            this.addKeyWord("[Int5]", "Random variable - random integer - Uniform discrete distribution U{-10^5, 10^5}", 7, "[Int5]", "3.0", 10);
            this.addKeyWord("[Int6]", "Random variable - random integer - Uniform discrete distribution U{-10^6, 10^6}", 8, "[Int6]", "3.0", 10);
            this.addKeyWord("[Int7]", "Random variable - random integer - Uniform discrete distribution U{-10^7, 10^7}", 9, "[Int7]", "3.0", 10);
            this.addKeyWord("[Int8]", "Random variable - random integer - Uniform discrete distribution U{-10^8, 10^8}", 10, "[Int8]", "3.0", 10);
            this.addKeyWord("[Int9]", "Random variable - random integer - Uniform discrete distribution U{-10^9, 10^9}", 11, "[Int9]", "3.0", 10);
            this.addKeyWord("[nat]", "Random variable - random natural number including 0", 12, "[nat]", "3.0", 10);
            this.addKeyWord("[nat1]", "Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^1}", 13, "[nat1]", "3.0", 10);
            this.addKeyWord("[nat2]", "Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^2}", 14, "[nat2]", "3.0", 10);
            this.addKeyWord("[nat3]", "Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^3}", 15, "[nat3]", "3.0", 10);
            this.addKeyWord("[nat4]", "Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^4}", 16, "[nat4]", "3.0", 10);
            this.addKeyWord("[nat5]", "Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^5}", 17, "[nat5]", "3.0", 10);
            this.addKeyWord("[nat6]", "Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^6}", 18, "[nat6]", "3.0", 10);
            this.addKeyWord("[nat7]", "Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^7}", 19, "[nat7]", "3.0", 10);
            this.addKeyWord("[nat8]", "Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^8}", 20, "[nat8]", "3.0", 10);
            this.addKeyWord("[nat9]", "Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^9}", 21, "[nat9]", "3.0", 10);
            this.addKeyWord("[Nat]", "Random variable - random natural number", 22, "[Nat]", "3.0", 10);
            this.addKeyWord("[Nat1]", "Random variable - random natural number - Uniform discrete distribution U{1, 10^1}", 23, "[Nat1]", "3.0", 10);
            this.addKeyWord("[Nat2]", "Random variable - random natural number - Uniform discrete distribution U{1, 10^2}", 24, "[Nat2]", "3.0", 10);
            this.addKeyWord("[Nat3]", "Random variable - random natural number - Uniform discrete distribution U{1, 10^3}", 25, "[Nat3]", "3.0", 10);
            this.addKeyWord("[Nat4]", "Random variable - random natural number - Uniform discrete distribution U{1, 10^4}", 26, "[Nat4]", "3.0", 10);
            this.addKeyWord("[Nat5]", "Random variable - random natural number - Uniform discrete distribution U{1, 10^5}", 27, "[Nat5]", "3.0", 10);
            this.addKeyWord("[Nat6]", "Random variable - random natural number - Uniform discrete distribution U{1, 10^6}", 28, "[Nat6]", "3.0", 10);
            this.addKeyWord("[Nat7]", "Random variable - random natural number - Uniform discrete distribution U{1, 10^7}", 29, "[Nat7]", "3.0", 10);
            this.addKeyWord("[Nat8]", "Random variable - random natural number - Uniform discrete distribution U{1, 10^8}", 30, "[Nat8]", "3.0", 10);
            this.addKeyWord("[Nat9]", "Random variable - random natural number - Uniform discrete distribution U{1, 10^9}", 31, "[Nat9]", "3.0", 10);
            this.addKeyWord("[Nor]", "Random variable - Normal distribution N(0,1)", 32, "[Nor]", "3.0", 10);
            this.addKeyWord("@~", "Bitwise unary complement", 1, "@~a", "4.0", 11);
            this.addKeyWord("@&", "Bitwise AND", 2, "a @& b", "4.0", 11);
            this.addKeyWord("@^", "Bitwise exclusive OR", 3, "a @^ b", "4.0", 11);
            this.addKeyWord("@|", "Bitwise inclusive OR", 4, "a @| b", "4.0", 11);
            this.addKeyWord("@<<", "Signed left shift", 5, "a @<< b", "4.0", 11);
            this.addKeyWord("@>>", "Signed right shift", 6, "a @>> b", "4.0", 11);
            this.addKeyWord("[%]", "<Ratio, Fraction> Percentage = 0.01", 1, "[%]", "4.0", 12);
            this.addKeyWord("[%%]", "<Ratio, Fraction> Promil, Per mille = 0.001", 2, "[%%]", "4.0", 12);
            this.addKeyWord("[Y]", "<Metric prefix> Septillion / Yotta = 10^24", 101, "[Y]", "4.0", 12);
            this.addKeyWord("[sept]", "<Metric prefix> Septillion / Yotta = 10^24", 101, "[sept]", "4.0", 12);
            this.addKeyWord("[Z]", "<Metric prefix> Sextillion / Zetta = 10^21", 102, "[Z]", "4.0", 12);
            this.addKeyWord("[sext]", "<Metric prefix> Sextillion / Zetta = 10^21", 102, "[sext]", "4.0", 12);
            this.addKeyWord("[E]", "<Metric prefix> Quintillion / Exa = 10^18", 103, "[E]", "4.0", 12);
            this.addKeyWord("[quint]", "<Metric prefix> Quintillion / Exa = 10^18", 103, "[quint]", "4.0", 12);
            this.addKeyWord("[P]", "<Metric prefix> Quadrillion / Peta = 10^15", 104, "[P]", "4.0", 12);
            this.addKeyWord("[quad]", "<Metric prefix> Quadrillion / Peta = 10^15", 104, "[quad]", "4.0", 12);
            this.addKeyWord("[T]", "<Metric prefix> Trillion / Tera = 10^12", 105, "[T]", "4.0", 12);
            this.addKeyWord("[tril]", "<Metric prefix> Trillion / Tera = 10^12", 105, "[tril]", "4.0", 12);
            this.addKeyWord("[G]", "<Metric prefix> Billion / Giga = 10^9", 106, "[G]", "4.0", 12);
            this.addKeyWord("[bil]", "<Metric prefix> Billion / Giga = 10^9", 106, "[bil]", "4.0", 12);
            this.addKeyWord("[M]", "<Metric prefix> Million / Mega = 10^6", 107, "[M]", "4.0", 12);
            this.addKeyWord("[mil]", "<Metric prefix> Million / Mega = 10^6", 107, "[mil]", "4.0", 12);
            this.addKeyWord("[k]", "<Metric prefix> Thousand / Kilo = 10^3", 108, "[k]", "4.0", 12);
            this.addKeyWord("[th]", "<Metric prefix> Thousand / Kilo = 10^3", 108, "[th]", "4.0", 12);
            this.addKeyWord("[hecto]", "<Metric prefix> Hundred / Hecto = 10^2", 109, "[hecto]", "4.0", 12);
            this.addKeyWord("[hund]", "<Metric prefix> Hundred / Hecto = 10^2", 109, "[hund]", "4.0", 12);
            this.addKeyWord("[deca]", "<Metric prefix> Ten / Deca = 10", 110, "[deca]", "4.0", 12);
            this.addKeyWord("[ten]", "<Metric prefix> Ten / Deca = 10", 110, "[ten]", "4.0", 12);
            this.addKeyWord("[deci]", "<Metric prefix> Tenth / Deci = 0.1", 111, "[deci]", "4.0", 12);
            this.addKeyWord("[centi]", "<Metric prefix> Hundredth / Centi = 0.01", 112, "[centi]", "4.0", 12);
            this.addKeyWord("[milli]", "<Metric prefix> Thousandth / Milli = 0.001", 113, "[milli]", "4.0", 12);
            this.addKeyWord("[mic]", "<Metric prefix> Millionth / Micro = 10^-6", 114, "[mic]", "4.0", 12);
            this.addKeyWord("[n]", "<Metric prefix> Billionth / Nano = 10^-9", 115, "[n]", "4.0", 12);
            this.addKeyWord("[p]", "<Metric prefix> Trillionth / Pico = 10^-12", 116, "[p]", "4.0", 12);
            this.addKeyWord("[f]", "<Metric prefix> Quadrillionth / Femto = 10^-15", 117, "[f]", "4.0", 12);
            this.addKeyWord("[a]", "<Metric prefix> Quintillionth / Atoo = 10^-18", 118, "[a]", "4.0", 12);
            this.addKeyWord("[z]", "<Metric prefix> Sextillionth / Zepto = 10^-21", 119, "[z]", "4.0", 12);
            this.addKeyWord("[y]", "<Metric prefix> Septillionth / Yocto = 10^-24", 120, "[y]", "4.0", 12);
            this.addKeyWord("[m]", "<Unit of length> Metre / Meter (m=1)", 201, "[m]", "4.0", 12);
            this.addKeyWord("[km]", "<Unit of length> Kilometre / Kilometer (m=1)", 202, "[km]", "4.0", 12);
            this.addKeyWord("[cm]", "<Unit of length> Centimetre / Centimeter (m=1)", 203, "[cm]", "4.0", 12);
            this.addKeyWord("[mm]", "<Unit of length> Millimetre / Millimeter (m=1)", 204, "[mm]", "4.0", 12);
            this.addKeyWord("[inch]", "<Unit of length> Inch (m=1)", 205, "[inch]", "4.0", 12);
            this.addKeyWord("[yd]", "<Unit of length> Yard (m=1)", 206, "[yd]", "4.0", 12);
            this.addKeyWord("[ft]", "<Unit of length> Feet (m=1)", 207, "[ft]", "4.0", 12);
            this.addKeyWord("[mile]", "<Unit of length> Mile (m=1)", 208, "[mile]", "4.0", 12);
            this.addKeyWord("[nmi]", "<Unit of length> Nautical mile (m=1)", 209, "[nmi]", "4.0", 12);
            this.addKeyWord("[m2]", "<Unit of area> Square metre / Square meter (m=1)", 301, "[m2]", "4.0", 12);
            this.addKeyWord("[cm2]", "<Unit of area> Square centimetre / Square centimeter (m=1)", 302, "[cm2]", "4.0", 12);
            this.addKeyWord("[mm2]", "<Unit of area> Square millimetre / Square millimeter (m=1)", 303, "[mm2]", "4.0", 12);
            this.addKeyWord("[are]", "<Unit of area> Are (m=1)", 304, "[are]", "4.0", 12);
            this.addKeyWord("[ha]", "<Unit of area> Hectare (m=1)", 305, "[ha]", "4.0", 12);
            this.addKeyWord("[acre]", "<Unit of area> Acre (m=1)", 306, "[acre]", "4.0", 12);
            this.addKeyWord("[km2]", "<Unit of area> Square kilometre / Square kilometer (m=1)", 307, "[km2]", "4.0", 12);
            this.addKeyWord("[mm3]", "<Unit of volume> Cubic millimetre / Cubic millimeter (m=1)", 401, "[mm3]", "4.0", 12);
            this.addKeyWord("[cm3]", "<Unit of volume> Cubic centimetre / Cubic centimeter (m=1)", 402, "[cm3]", "4.0", 12);
            this.addKeyWord("[m3]", "<Unit of volume> Cubic metre / Cubic meter (m=1)", 403, "[m3]", "4.0", 12);
            this.addKeyWord("[km3]", "<Unit of volume> Cubic kilometre / Cubic kilometer (m=1)", 404, "[km3]", "4.0", 12);
            this.addKeyWord("[ml]", "<Unit of volume> Millilitre / Milliliter (m=1)", 405, "[ml]", "4.0", 12);
            this.addKeyWord("[l]", "<Unit of volume> Litre / Liter (m=1)", 406, "[l]", "4.0", 12);
            this.addKeyWord("[gall]", "<Unit of volume> Gallon (m=1)", 407, "[gall]", "4.0", 12);
            this.addKeyWord("[pint]", "<Unit of volume> Pint (m=1)", 408, "[pint]", "4.0", 12);
            this.addKeyWord("[s]", "<Unit of time> Second (s=1)", 501, "[s]", "4.0", 12);
            this.addKeyWord("[ms]", "<Unit of time> Millisecond (s=1)", 502, "[ms]", "4.0", 12);
            this.addKeyWord("[min]", "<Unit of time> Minute (s=1)", 503, "[min]", "4.0", 12);
            this.addKeyWord("[h]", "<Unit of time> Hour (s=1)", 504, "[h]", "4.0", 12);
            this.addKeyWord("[day]", "<Unit of time> Day (s=1)", 505, "[day]", "4.0", 12);
            this.addKeyWord("[week]", "<Unit of time> Week (s=1)", 506, "[week]", "4.0", 12);
            this.addKeyWord("[yearj]", "<Unit of time> Julian year = 365.25 days (s=1)", 507, "[yearj]", "4.0", 12);
            this.addKeyWord("[kg]", "<Unit of mass> Kilogram (kg=1)", 508, "[kg]", "4.0", 12);
            this.addKeyWord("[gr]", "<Unit of mass> Gram (kg=1)", 509, "[gr]", "4.0", 12);
            this.addKeyWord("[mg]", "<Unit of mass> Milligram (kg=1)", 510, "[mg]", "4.0", 12);
            this.addKeyWord("[dag]", "<Unit of mass> Decagram (kg=1)", 511, "[dag]", "4.0", 12);
            this.addKeyWord("[t]", "<Unit of mass> Tonne (kg=1)", 512, "[t]", "4.0", 12);
            this.addKeyWord("[oz]", "<Unit of mass> Ounce (kg=1)", 513, "[oz]", "4.0", 12);
            this.addKeyWord("[lb]", "<Unit of mass> Pound (kg=1)", 514, "[lb]", "4.0", 12);
            this.addKeyWord("[b]", "<Unit of information> Bit (bit=1)", 601, "[b]", "4.0", 12);
            this.addKeyWord("[kb]", "<Unit of information> Kilobit (bit=1)", 602, "[kb]", "4.0", 12);
            this.addKeyWord("[Mb]", "<Unit of information> Megabit (bit=1)", 603, "[Mb]", "4.0", 12);
            this.addKeyWord("[Gb]", "<Unit of information> Gigabit (bit=1)", 604, "[Gb]", "4.0", 12);
            this.addKeyWord("[Tb]", "<Unit of information> Terabit (bit=1)", 605, "[Tb]", "4.0", 12);
            this.addKeyWord("[Pb]", "<Unit of information> Petabit (bit=1)", 606, "[Pb]", "4.0", 12);
            this.addKeyWord("[Eb]", "<Unit of information> Exabit (bit=1)", 607, "[Eb]", "4.0", 12);
            this.addKeyWord("[Zb]", "<Unit of information> Zettabit (bit=1)", 608, "[Zb]", "4.0", 12);
            this.addKeyWord("[Yb]", "<Unit of information> Yottabit (bit=1)", 609, "[Yb]", "4.0", 12);
            this.addKeyWord("[B]", "<Unit of information> Byte (bit=1)", 610, "[B]", "4.0", 12);
            this.addKeyWord("[kB]", "<Unit of information> Kilobyte (bit=1)", 611, "[kB]", "4.0", 12);
            this.addKeyWord("[MB]", "<Unit of information> Megabyte (bit=1)", 612, "[MB]", "4.0", 12);
            this.addKeyWord("[GB]", "<Unit of information> Gigabyte (bit=1)", 613, "[GB]", "4.0", 12);
            this.addKeyWord("[TB]", "<Unit of information> Terabyte (bit=1)", 614, "[TB]", "4.0", 12);
            this.addKeyWord("[PB]", "<Unit of information> Petabyte (bit=1)", 615, "[PB]", "4.0", 12);
            this.addKeyWord("[EB]", "<Unit of information> Exabyte (bit=1)", 616, "[EB]", "4.0", 12);
            this.addKeyWord("[ZB]", "<Unit of information> Zettabyte (bit=1)", 617, "[ZB]", "4.0", 12);
            this.addKeyWord("[YB]", "<Unit of information> Yottabyte (bit=1)", 618, "[YB]", "4.0", 12);
            this.addKeyWord("[J]", "<Unit of energy> Joule (m=1, kg=1, s=1)", 701, "[J]", "4.0", 12);
            this.addKeyWord("[eV]", "<Unit of energy> Electronovolt (m=1, kg=1, s=1)", 702, "[eV]", "4.0", 12);
            this.addKeyWord("[keV]", "<Unit of energy> Kiloelectronovolt (m=1, kg=1, s=1)", 703, "[keV]", "4.0", 12);
            this.addKeyWord("[MeV]", "<Unit of energy> Megaelectronovolt (m=1, kg=1, s=1)", 704, "[MeV]", "4.0", 12);
            this.addKeyWord("[GeV]", "<Unit of energy> Gigaelectronovolt (m=1, kg=1, s=1)", 705, "[GeV]", "4.0", 12);
            this.addKeyWord("[TeV]", "<Unit of energy> Teraelectronovolt (m=1, kg=1, s=1)", 706, "[TeV]", "4.0", 12);
            this.addKeyWord("[m/s]", "<Unit of speed> Metre / Meter per second (m=1, s=1)", 801, "[m/s]", "4.0", 12);
            this.addKeyWord("[km/h]", "<Unit of speed> Kilometre / Kilometer per hour (m=1, s=1)", 802, "[km/h]", "4.0", 12);
            this.addKeyWord("[mi/h]", "<Unit of speed> Mile per hour (m=1, s=1)", 803, "[mi/h]", "4.0", 12);
            this.addKeyWord("[knot]", "<Unit of speed> Knot (m=1, s=1)", 804, "[knot]", "4.0", 12);
            this.addKeyWord("[m/s2]", "<Unit of acceleration> Metre / Meter per square second (m=1, s=1)", 901, "[m/s2]", "4.0", 12);
            this.addKeyWord("[km/h2]", "<Unit of acceleration> Kilometre / Kilometer per square hour (m=1, s=1)", 902, "[km/h2]", "4.0", 12);
            this.addKeyWord("[mi/h2]", "<Unit of acceleration> Mile per square hour (m=1, s=1)", 903, "[mi/h2]", "4.0", 12);
            this.addKeyWord("[rad]", "<Unit of angle> Radian (rad=1)", 1001, "[rad]", "4.0", 12);
            this.addKeyWord("[deg]", "<Unit of angle> Degree of arc (rad=1)", 1002, "[deg]", "4.0", 12);
            this.addKeyWord("[']", "<Unit of angle> Minute of arc (rad=1)", 1003, "[']", "4.0", 12);
            this.addKeyWord("['']", "<Unit of angle> Second of arc (rad=1)", 1004, "['']", "4.0", 12);
            if (this.UDFExpression) {
                this.addUDFSpecificParserKeyWords();
            }
        }

        this.addKeyWord("(", "Left parentheses", 1, "( ... )", "1.0", 20);
        this.addKeyWord(")", "Right parentheses", 2, "( ... )", "1.0", 20);
        this.addKeyWord(",", "Comma (function parameters)", 3, "(a1, ... ,an)", "1.0", 20);
        this.addKeyWord(";", "Semicolon (function parameters)", 3, "(a1; ... ;an)", "1.0", 20);
        this.addKeyWord("[+-]?(([0-9]([0-9])*)?\\.[0-9]([0-9])*|[0-9]([0-9])*)([eE][+-]?[0-9]([0-9])*)?", "Regullar expression for decimal numbers", 1, "Integer (since v.1.0): 1, -2; Decimal (since v.1.0): 0.2, -0.3, 1.2; Leading zero (since v.4.1): 001, -002.1; Scientific notation (since v.4.2): 1.2e-10, 1.2e+10, 2.3e10; No leading zero (since v.4.2): .2, -.212; Fractions (since v.4.2): 1_2, 2_1_3, 14_3; Other systems (since v.4.1): b1.111, b2.1001, b3.12021, b16.af12, h.af1, b.1001, o.0127", "1.0", 0);
        this.addKeyWord(" ", "Blank (whitespace) character", 4, " ", "4.2", 20);
    }

    private void addArgumentsKeyWords() {
        int var1 = this.argumentsList.size();

        for(int var2 = 0; var2 < var1; ++var2) {
            Argument var3 = (Argument)this.argumentsList.get(var2);
            if (var3.getArgumentType() != 3) {
                this.addKeyWord(var3.getArgumentName(), var3.getDescription(), var2, var3.getArgumentName(), "", 101);
            } else {
                this.addKeyWord(var3.getArgumentName(), var3.getDescription(), var2, var3.getArgumentName() + "(n)", "", 102);
            }
        }

    }

    private void addFunctionsKeyWords() {
        int var1 = this.functionsList.size();

        for(int var2 = 0; var2 < var1; ++var2) {
            Function var3 = (Function)this.functionsList.get(var2);
            String var4 = var3.getFunctionName() + "(";
            int var5 = var3.getParametersNumber();

            for(int var6 = 0; var6 < var5; ++var6) {
                var4 = var4 + var3.getParameterName(var6);
                if (var5 > 1 && var6 < var5 - 1) {
                    var4 = var4 + ",";
                }
            }

            var4 = var4 + ")";
            this.addKeyWord(var3.getFunctionName(), var3.getDescription(), var2, var4, "", 103);
        }

    }

    private void addConstantsKeyWords() {
        int var1 = this.constantsList.size();

        for(int var2 = 0; var2 < var1; ++var2) {
            Constant var3 = (Constant)this.constantsList.get(var2);
            this.addKeyWord(var3.getConstantName(), var3.getDescription(), var2, var3.getConstantName(), "", 104);
        }

    }

    private void validateParserKeyWords() {
        if (mXparser.overrideBuiltinTokens) {
            ArrayList var1 = new ArrayList();
            Iterator var2 = this.argumentsList.iterator();

            while(var2.hasNext()) {
                Argument var3 = (Argument)var2.next();
                var1.add(var3.getArgumentName());
            }

            var2 = this.functionsList.iterator();

            while(var2.hasNext()) {
                Function var6 = (Function)var2.next();
                var1.add(var6.getFunctionName());
            }

            var2 = this.constantsList.iterator();

            while(var2.hasNext()) {
                Constant var7 = (Constant)var2.next();
                var1.add(var7.getConstantName());
            }

            if (var1.isEmpty()) {
                return;
            }

            ArrayList var5 = new ArrayList();
            Iterator var8 = this.keyWordsList.iterator();

            KeyWord var4;
            while(var8.hasNext()) {
                var4 = (KeyWord)var8.next();
                if (var1.contains(var4.wordString)) {
                    var5.add(var4);
                }
            }

            if (var5.isEmpty()) {
                return;
            }

            var8 = var5.iterator();

            while(var8.hasNext()) {
                var4 = (KeyWord)var8.next();
                this.keyWordsList.remove(var4);
            }
        }

    }

    private void addKeyWord(String var1, String var2, int var3, String var4, String var5, int var6) {
        if ((mXparser.tokensToRemove.size() > 0 || mXparser.tokensToModify.size() > 0) && (var6 == 4 || var6 == 5 || var6 == 6 || var6 == 7 || var6 == 8 || var6 == 9 || var6 == 10 || var6 == 12)) {
            if (mXparser.tokensToRemove.size() > 0 && mXparser.tokensToRemove.contains(var1)) {
                return;
            }

            if (mXparser.tokensToModify.size() > 0) {
                Iterator var7 = mXparser.tokensToModify.iterator();

                while(var7.hasNext()) {
                    TokenModification var8 = (TokenModification)var7.next();
                    if (var8.currentToken.equals(var1)) {
                        var1 = var8.newToken;
                        if (var8.newTokenDescription != null) {
                            var2 = var8.newTokenDescription;
                        }

                        var4 = var4.replace(var8.currentToken, var8.newToken);
                    }
                }
            }
        }

        this.keyWordsList.add(new KeyWord(var1, var2, var3, var4, var5, var6));
    }

    private void checkOtherNumberBases(Token var1) {
        byte var2 = 0;
        int var3 = var1.tokenStr.length();
        if (var3 >= 2 && var1.tokenStr.charAt(1) == '.') {
            var2 = 1;
        }

        if (var2 == 0 && var3 >= 3 && var1.tokenStr.charAt(2) == '.') {
            var2 = 2;
        }

        if (var2 == 0 && var3 >= 4 && var1.tokenStr.charAt(3) == '.') {
            var2 = 3;
        }

        if (var2 != 0) {
            String var4 = var1.tokenStr.substring(0, var2).toLowerCase();
            String var5 = "";
            if (var3 > var2 + 1) {
                var5 = var1.tokenStr.substring(var2 + 1);
            }

            byte var6 = 0;
            if (var4.equals("b")) {
                var6 = 2;
            } else if (var4.equals("o")) {
                var6 = 8;
            } else if (var4.equals("h")) {
                var6 = 16;
            } else if (var4.equals("b1")) {
                var6 = 1;
            } else if (var4.equals("b2")) {
                var6 = 2;
            } else if (var4.equals("b3")) {
                var6 = 3;
            } else if (var4.equals("b4")) {
                var6 = 4;
            } else if (var4.equals("b5")) {
                var6 = 5;
            } else if (var4.equals("b6")) {
                var6 = 6;
            } else if (var4.equals("b7")) {
                var6 = 7;
            } else if (var4.equals("b8")) {
                var6 = 8;
            } else if (var4.equals("b9")) {
                var6 = 9;
            } else if (var4.equals("b10")) {
                var6 = 10;
            } else if (var4.equals("b11")) {
                var6 = 11;
            } else if (var4.equals("b12")) {
                var6 = 12;
            } else if (var4.equals("b13")) {
                var6 = 13;
            } else if (var4.equals("b14")) {
                var6 = 14;
            } else if (var4.equals("b15")) {
                var6 = 15;
            } else if (var4.equals("b16")) {
                var6 = 16;
            } else if (var4.equals("b17")) {
                var6 = 17;
            } else if (var4.equals("b18")) {
                var6 = 18;
            } else if (var4.equals("b19")) {
                var6 = 19;
            } else if (var4.equals("b20")) {
                var6 = 20;
            } else if (var4.equals("b21")) {
                var6 = 21;
            } else if (var4.equals("b22")) {
                var6 = 22;
            } else if (var4.equals("b23")) {
                var6 = 23;
            } else if (var4.equals("b24")) {
                var6 = 24;
            } else if (var4.equals("b25")) {
                var6 = 25;
            } else if (var4.equals("b26")) {
                var6 = 26;
            } else if (var4.equals("b27")) {
                var6 = 27;
            } else if (var4.equals("b28")) {
                var6 = 28;
            } else if (var4.equals("b29")) {
                var6 = 29;
            } else if (var4.equals("b30")) {
                var6 = 30;
            } else if (var4.equals("b31")) {
                var6 = 31;
            } else if (var4.equals("b32")) {
                var6 = 32;
            } else if (var4.equals("b33")) {
                var6 = 33;
            } else if (var4.equals("b34")) {
                var6 = 34;
            } else if (var4.equals("b35")) {
                var6 = 35;
            } else if (var4.equals("b36")) {
                var6 = 36;
            }

            if (var6 > 0 && var6 <= 36) {
                var1.tokenTypeId = 0;
                var1.tokenId = 1;
                var1.tokenValue = NumberTheory.convOthBase2Decimal(var5, var6);
            }

        }
    }

    private void checkFraction(Token var1) {
        int var2 = var1.tokenStr.length();
        if (var2 >= 3) {
            if (mXparser.regexMatch(var1.tokenStr, "([0-9]([0-9])*\\_)?[0-9]([0-9])*\\_[0-9]([0-9])*")) {
                int var3 = var1.tokenStr.indexOf(95);
                int var4 = var1.tokenStr.indexOf(95, var3 + 1);
                boolean var5 = false;
                if (var4 > 0) {
                    var5 = true;
                }

                double var6;
                String var8;
                String var9;
                if (var5) {
                    var8 = var1.tokenStr.substring(0, var3);
                    var9 = var1.tokenStr.substring(var3 + 1, var4);
                    String var10 = var1.tokenStr.substring(var4 + 1);
                    double var11 = Double.parseDouble(var8);
                    double var13 = Double.parseDouble(var9);
                    double var15 = Double.parseDouble(var10);
                    if (var15 == 0.0D) {
                        var6 = 0.0D / 0.0;
                    } else {
                        var6 = var11 + var13 / var15;
                    }
                } else {
                    var8 = var1.tokenStr.substring(0, var3);
                    var9 = var1.tokenStr.substring(var3 + 1);
                    double var17 = Double.parseDouble(var8);
                    double var12 = Double.parseDouble(var9);
                    if (var12 == 0.0D) {
                        var6 = 0.0D / 0.0;
                    } else {
                        var6 = var17 / var12;
                    }
                }

                var1.tokenTypeId = 0;
                var1.tokenId = 1;
                var1.tokenValue = var6;
            }
        }
    }

    private void addToken(String var1, KeyWord var2) {
        Token var3 = new Token();
        this.initialTokens.add(var3);
        var3.tokenStr = var1;
        var3.keyWord = var2.wordString;
        var3.tokenTypeId = var2.wordTypeId;
        var3.tokenId = var2.wordId;
        if (var3.tokenTypeId == 101) {
            var3.tokenValue = ((Argument)this.argumentsList.get(var3.tokenId)).argumentValue;
        } else if (var3.tokenTypeId == 0) {
            var3.tokenValue = Double.valueOf(var3.tokenStr);
            var3.keyWord = "_num_";
        } else if (var3.tokenTypeId == -1) {
            this.checkOtherNumberBases(var3);
            if (var3.tokenTypeId == -1) {
                this.checkFraction(var3);
            }
        }

    }

    private boolean isNotSpecialChar(char var1) {
        if (var1 == '+') {
            return false;
        } else if (var1 == '-') {
            return false;
        } else if (var1 == '+') {
            return false;
        } else if (var1 == '*') {
            return false;
        } else if (var1 == '/') {
            return false;
        } else if (var1 == '^') {
            return false;
        } else if (var1 == ',') {
            return false;
        } else if (var1 == ';') {
            return false;
        } else if (var1 == '(') {
            return false;
        } else if (var1 == ')') {
            return false;
        } else if (var1 == '|') {
            return false;
        } else if (var1 == '&') {
            return false;
        } else if (var1 == '=') {
            return false;
        } else if (var1 == '>') {
            return false;
        } else if (var1 == '<') {
            return false;
        } else if (var1 == '~') {
            return false;
        } else if (var1 == '\\') {
            return false;
        } else if (var1 == '#') {
            return false;
        } else {
            return var1 != '@';
        }
    }

    private void tokenizeExpressionString() {
        this.keyWordsList = new ArrayList();
        this.addParserKeyWords();
        this.validateParserKeyWords();
        if (!this.parserKeyWordsOnly) {
            this.addArgumentsKeyWords();
            this.addFunctionsKeyWords();
            this.addConstantsKeyWords();
        }

        Collections.sort(this.keyWordsList, new DescKwLenComparator());
        int var1 = -1;
        int var2 = -1;
        int var3 = -1;

        int var4;
        for(var4 = 0; var4 < this.keyWordsList.size(); ++var4) {
            if (((KeyWord)this.keyWordsList.get(var4)).wordTypeId == 0) {
                var1 = var4;
            }

            if (((KeyWord)this.keyWordsList.get(var4)).wordTypeId == 1) {
                if (((KeyWord)this.keyWordsList.get(var4)).wordId == 1) {
                    var2 = var4;
                }

                if (((KeyWord)this.keyWordsList.get(var4)).wordId == 2) {
                    var3 = var4;
                }
            }
        }

        this.initialTokens = new ArrayList();
        var4 = this.expressionString.length();
        if (var4 != 0) {
            String var5 = "";
            char var7 = 'a';
            int var8 = 0;
            int var9 = 0;

            char var6;
            int var10;
            for(var10 = 0; var10 < var4; ++var10) {
                var6 = this.expressionString.charAt(var10);
                if (var6 != ' ' && var6 != '\n' && var6 != '\r' && var6 != '\t' && var6 != '\f') {
                    if (var8 > 0) {
                        if (var9 > 0 && this.isNotSpecialChar(var7)) {
                            var5 = var5 + " ";
                        }

                        var8 = 0;
                    }
                } else {
                    ++var8;
                }

                if (var8 == 0) {
                    var5 = var5 + var6;
                    var7 = var6;
                    ++var9;
                }
            }

            if (var5.length() != 0) {
                var10 = 0;
                int var11 = 0;
                String var12 = "";
                byte var13 = -1;
                boolean var14 = true;
                KeyWord var15 = null;
                String var16 = "";
                String var17 = "";

                byte var24;
                do {
                    int var21 = -1;
                    char var20 = var5.charAt(var11);
                    int var22;
                    if (var20 == '+' || var20 == '-' || var20 == '.' || var20 == '0' || var20 == '1' || var20 == '2' || var20 == '3' || var20 == '4' || var20 == '5' || var20 == '6' || var20 == '7' || var20 == '8' || var20 == '9') {
                        for(var22 = var11; var22 < var5.length(); ++var22) {
                            if (var22 > var11) {
                                var6 = var5.charAt(var22);
                                if (var6 != '+' && var6 != '-' && var6 != '0' && var6 != '1' && var6 != '2' && var6 != '3' && var6 != '4' && var6 != '5' && var6 != '6' && var6 != '7' && var6 != '8' && var6 != '9' && var6 != '.' && var6 != 'e' && var6 != 'E') {
                                    break;
                                }
                            }

                            String var23 = var5.substring(var11, var22 + 1);
                            if (mXparser.regexMatch(var23, "[+-]?(([0-9]([0-9])*)?\\.[0-9]([0-9])*|[0-9]([0-9])*)([eE][+-]?[0-9]([0-9])*)?")) {
                                var21 = var22;
                            }
                        }
                    }

                    char var18;
                    if (var21 >= 0 && var11 > 0) {
                        var18 = var5.charAt(var11 - 1);
                        if (var18 != ' ' && var18 != ',' && var18 != ';' && var18 != '|' && var18 != '&' && var18 != '+' && var18 != '-' && var18 != '*' && var18 != '\\' && var18 != '/' && var18 != '(' && var18 != ')' && var18 != '=' && var18 != '>' && var18 != '<' && var18 != '~' && var18 != '^' && var18 != '#' && var18 != '%' && var18 != '@' && var18 != '!') {
                            var21 = -1;
                        }
                    }

                    char var19;
                    if (var21 >= 0 && var21 < var5.length() - 1) {
                        var19 = var5.charAt(var21 + 1);
                        if (var19 != ' ' && var19 != ',' && var19 != ';' && var19 != '|' && var19 != '&' && var19 != '+' && var19 != '-' && var19 != '*' && var19 != '\\' && var19 != '/' && var19 != '(' && var19 != ')' && var19 != '=' && var19 != '>' && var19 != '<' && var19 != '~' && var19 != '^' && var19 != '#' && var19 != '%' && var19 != '@' && var19 != '!') {
                            var21 = -1;
                        }
                    }

                    if (var21 >= 0) {
                        if (var13 == -1 && var11 > 0) {
                            var12 = var5.substring(var10, var11);
                            this.addToken(var12, new KeyWord());
                        }

                        var20 = var5.charAt(var11);
                        boolean var25 = true;
                        if (var20 != '-' && var20 != '+') {
                            var25 = false;
                        } else if (this.initialTokens.size() > 0) {
                            Token var26 = (Token)this.initialTokens.get(this.initialTokens.size() - 1);
                            if ((var26.tokenTypeId != 1 || var26.tokenId == 6 || var26.tokenId == 8) && var26.tokenTypeId != 3 && var26.tokenTypeId != 2 && var26.tokenTypeId != 11 && (var26.tokenTypeId != 20 || var26.tokenId != 1)) {
                                var25 = true;
                            } else {
                                var25 = false;
                            }
                        } else {
                            var25 = false;
                        }

                        if (var25) {
                            if (var20 == '-') {
                                this.addToken("-", (KeyWord)this.keyWordsList.get(var3));
                            }

                            if (var20 == '+') {
                                this.addToken("+", (KeyWord)this.keyWordsList.get(var2));
                            }

                            ++var11;
                        }

                        var12 = var5.substring(var11, var21 + 1);
                        this.addToken(var12, (KeyWord)this.keyWordsList.get(var1));
                        var11 = var21 + 1;
                        var10 = var11;
                        var24 = 0;
                        var13 = 0;
                    } else {
                        var22 = -1;
                        var24 = -1;

                        do {
                            ++var22;
                            var15 = (KeyWord)this.keyWordsList.get(var22);
                            var17 = var15.wordString;
                            if (var11 + var17.length() <= var5.length()) {
                                var16 = var5.substring(var11, var11 + var17.length());
                                if (var16.equals(var17)) {
                                    var24 = 0;
                                }

                                if (var24 == 0 && (var15.wordTypeId == 101 || var15.wordTypeId == 102 || var15.wordTypeId == 4 || var15.wordTypeId == 5 || var15.wordTypeId == 6 || var15.wordTypeId == 7 || var15.wordTypeId == 9 || var15.wordTypeId == 104 || var15.wordTypeId == 10 || var15.wordTypeId == 12 || var15.wordTypeId == 103 || var15.wordTypeId == 8)) {
                                    if (var11 > 0) {
                                        var18 = var5.charAt(var11 - 1);
                                        if (var18 != ' ' && var18 != ',' && var18 != ';' && var18 != '|' && var18 != '&' && var18 != '+' && var18 != '-' && var18 != '*' && var18 != '\\' && var18 != '/' && var18 != '(' && var18 != ')' && var18 != '=' && var18 != '>' && var18 != '<' && var18 != '~' && var18 != '^' && var18 != '#' && var18 != '%' && var18 != '@' && var18 != '!') {
                                            var24 = -1;
                                        }
                                    }

                                    if (var24 == 0 && var11 + var17.length() < var5.length()) {
                                        var19 = var5.charAt(var11 + var17.length());
                                        if (var19 != ' ' && var19 != ',' && var19 != ';' && var19 != '|' && var19 != '&' && var19 != '+' && var19 != '-' && var19 != '*' && var19 != '\\' && var19 != '/' && var19 != '(' && var19 != ')' && var19 != '=' && var19 != '>' && var19 != '<' && var19 != '~' && var19 != '^' && var19 != '#' && var19 != '%' && var19 != '@' && var19 != '!') {
                                            var24 = -1;
                                        }
                                    }
                                }
                            }
                        } while(var22 < this.keyWordsList.size() - 1 && var24 == -1);

                        if (var24 == 0) {
                            if (var13 == -1 && var11 > 0) {
                                var12 = var5.substring(var10, var11);
                                this.addToken(var12, new KeyWord());
                            }

                            var13 = 0;
                            var12 = var5.substring(var11, var11 + var17.length());
                            if (var15.wordTypeId != 20 || var15.wordId != 4) {
                                this.addToken(var12, var15);
                            }

                            var10 = var11 + var17.length();
                            var11 += var17.length();
                        } else {
                            var13 = -1;
                            if (var11 < var5.length()) {
                                ++var11;
                            }
                        }
                    }
                } while(var11 < var5.length());

                if (var24 == -1) {
                    var12 = var5.substring(var10, var11);
                    this.addToken(var12, new KeyWord());
                }

                this.evaluateTokensLevels();
            }
        }
    }

    private void evaluateTokensLevels() {
        int var1 = 0;
        Stack var2 = new Stack();
        boolean var3 = false;
        if (this.initialTokens.size() > 0) {
            for(int var4 = 0; var4 < this.initialTokens.size(); ++var4) {
                Token var5 = (Token)this.initialTokens.get(var4);
                TokenStackElement var6;
                if (var5.tokenTypeId != 4 && var5.tokenTypeId != 5 && var5.tokenTypeId != 6 && var5.tokenTypeId != 103 && var5.tokenTypeId != 8 && var5.tokenTypeId != 102 && var5.tokenTypeId != 7) {
                    if (var5.tokenTypeId == 20 && var5.tokenId == 1) {
                        ++var1;
                        var6 = new TokenStackElement();
                        var6.tokenId = var5.tokenId;
                        var6.tokenIndex = var4;
                        var6.tokenLevel = var1;
                        var6.tokenTypeId = var5.tokenTypeId;
                        var6.precedingFunction = var3;
                        var2.push(var6);
                        var3 = false;
                    } else {
                        var3 = false;
                    }
                } else {
                    ++var1;
                    var3 = true;
                }

                var5.tokenLevel = var1;
                if (var5.tokenTypeId == 20 && var5.tokenId == 2) {
                    --var1;
                    if (!var2.isEmpty()) {
                        var6 = (TokenStackElement)var2.pop();
                        if (var6.precedingFunction) {
                            --var1;
                        }
                    }
                }
            }
        }

    }

    private void copyInitialTokens() {
        this.tokensList = new ArrayList();
        Iterator var1 = this.initialTokens.iterator();

        while(var1.hasNext()) {
            Token var2 = (Token)var1.next();
            this.tokensList.add(var2.clone());
        }

    }

    public List<Token> getCopyOfInitialTokens() {
        ArrayList var1 = new ArrayList();
        if (this.expressionString.length() == 0) {
            return var1;
        } else {
            this.tokenizeExpressionString();
            if (this.initialTokens.size() == 0) {
                return var1;
            } else {
                for(int var3 = 0; var3 < this.initialTokens.size(); ++var3) {
                    Token var2 = (Token)this.initialTokens.get(var3);
                    if (var2.tokenTypeId == -1) {
                        if (mXparser.regexMatch(var2.tokenStr, "\\[([a-zA-Z_])+([a-zA-Z0-9_])*\\]")) {
                            var2.looksLike = "unit/const";
                        } else if (mXparser.regexMatch(var2.tokenStr, "([a-zA-Z_])+([a-zA-Z0-9_])*")) {
                            var2.looksLike = "argument";
                            if (var3 < this.initialTokens.size() - 1) {
                                Token var4 = (Token)this.initialTokens.get(var3 + 1);
                                if (var4.tokenTypeId == 20 && var4.tokenId == 1) {
                                    var2.looksLike = "function";
                                }
                            }
                        } else {
                            var2.looksLike = "error";
                        }
                    }

                    var1.add(var2.clone());
                }

                return var1;
            }
        }
    }

    public String[] getMissingUserDefinedArguments() {
        List var1 = this.getCopyOfInitialTokens();
        ArrayList var2 = new ArrayList();
        Iterator var3 = var1.iterator();

        while(var3.hasNext()) {
            Token var4 = (Token)var3.next();
            if (var4.looksLike.equals("argument") && !var2.contains(var4.tokenStr)) {
                var2.add(var4.tokenStr);
            }
        }

        int var6 = var2.size();
        String[] var7 = new String[var6];

        for(int var5 = 0; var5 < var6; ++var5) {
            var7[var5] = (String)var2.get(var5);
        }

        return var7;
    }

    public String[] getMissingUserDefinedUnits() {
        List var1 = this.getCopyOfInitialTokens();
        ArrayList var2 = new ArrayList();
        Iterator var3 = var1.iterator();

        while(var3.hasNext()) {
            Token var4 = (Token)var3.next();
            if (var4.looksLike.equals("unit/const") && !var2.contains(var4.tokenStr)) {
                var2.add(var4.tokenStr);
            }
        }

        int var6 = var2.size();
        String[] var7 = new String[var6];

        for(int var5 = 0; var5 < var6; ++var5) {
            var7[var5] = (String)var2.get(var5);
        }

        return var7;
    }

    public String[] getMissingUserDefinedFunctions() {
        List var1 = this.getCopyOfInitialTokens();
        ArrayList var2 = new ArrayList();
        Iterator var3 = var1.iterator();

        while(var3.hasNext()) {
            Token var4 = (Token)var3.next();
            if (var4.looksLike.equals("function") && !var2.contains(var4.tokenStr)) {
                var2.add(var4.tokenStr);
            }
        }

        int var6 = var2.size();
        String[] var7 = new String[var6];

        for(int var5 = 0; var5 < var6; ++var5) {
            var7[var5] = (String)var2.get(var5);
        }

        return var7;
    }

    List<Token> getInitialTokens() {
        return this.initialTokens;
    }

    private static final String getLeftSpaces(String var0, String var1) {
        String var2 = "";

        for(int var3 = 0; var3 < var0.length() - var1.length(); ++var3) {
            var2 = var2 + " ";
        }

        return var2 + var1;
    }

    private static final String getRightSpaces(String var0, String var1) {
        String var2 = "";

        for(int var3 = 0; var3 < var0.length() - var1.length(); ++var3) {
            var2 = " " + var2;
        }

        return var1 + var2;
    }

    private void showParsing(int var1, int var2) {
        mXparser.consolePrint(" ---> ");

        for(int var3 = var1; var3 <= var2; ++var3) {
            Token var4 = (Token)this.tokensList.get(var3);
            if (var4.tokenTypeId == 0) {
                mXparser.consolePrint(var4.tokenValue + " ");
            } else {
                mXparser.consolePrint(var4.tokenStr + " ");
            }
        }

        mXparser.consolePrint(" ... ");
    }

    void showKeyWords() {
        int var1 = this.keyWordsList.size();
        String var2 = "KEY_WORD";
        mXparser.consolePrintln("KEY WORDS:");
        mXparser.consolePrintln(" -------------------------------------------");
        mXparser.consolePrintln("|      IDX | KEY_WORD |       ID |  TYPE_ID |");
        mXparser.consolePrintln(" -------------------------------------------");

        for(int var3 = 0; var3 < var1; ++var3) {
            KeyWord var4 = (KeyWord)this.keyWordsList.get(var3);
            String var5 = getLeftSpaces(var2, Integer.toString(var3));
            String var6 = getLeftSpaces(var2, var4.wordString);
            String var7 = getLeftSpaces(var2, Integer.toString(var4.wordId));
            String var8 = getLeftSpaces(var2, Integer.toString(var4.wordTypeId));
            mXparser.consolePrintln("| " + var5 + " | " + var6 + " | " + var7 + " | " + var8 + " |");
        }

        mXparser.consolePrintln(" -------------------------------------------");
    }

    public String getHelp() {
        return this.getHelp("");
    }

    public String getHelp(String var1) {
        this.keyWordsList = new ArrayList();
        String var2 = "Help content: \n\n";
        this.addParserKeyWords();
        this.validateParserKeyWords();
        if (!this.parserKeyWordsOnly) {
            this.addArgumentsKeyWords();
            this.addFunctionsKeyWords();
            this.addConstantsKeyWords();
        }

        var2 = var2 + getLeftSpaces("12345", "#") + "  " + getRightSpaces("01234567890123456789", "key word") + getRightSpaces("                        ", "type") + getRightSpaces("0123456789012345678901234567890123456789012345", "syntax") + getRightSpaces("012345", "since") + "description\n";
        var2 = var2 + getLeftSpaces("12345", "-") + "  " + getRightSpaces("01234567890123456789", "--------") + getRightSpaces("                        ", "----") + getRightSpaces("0123456789012345678901234567890123456789012345", "------") + getRightSpaces("012345", "-----") + "-----------\n";
        Collections.sort(this.keyWordsList, new KwTypeComparator());
        int var3 = this.keyWordsList.size();

        for(int var7 = 0; var7 < var3; ++var7) {
            KeyWord var8 = (KeyWord)this.keyWordsList.get(var7);
            String var4 = "";
            String var5 = var8.wordString;
            switch(var8.wordTypeId) {
                case 0:
                    var4 = "number";
                    var5 = "_number_";
                    break;
                case 1:
                    var4 = "Operator";
                    break;
                case 2:
                    var4 = "Boolean Operator";
                    break;
                case 3:
                    var4 = "Binary Relation";
                    break;
                case 4:
                    var4 = "Unary Function";
                    break;
                case 5:
                    var4 = "Binary Function";
                    break;
                case 6:
                    var4 = "3-args Function";
                    break;
                case 7:
                    var4 = "Variadic Function";
                    break;
                case 8:
                    var4 = "Calculus Operator";
                    break;
                case 9:
                    var4 = "Constant Value";
                    break;
                case 10:
                    var4 = "Random Variable";
                    break;
                case 11:
                    var4 = "Bitwise Operator";
                    break;
                case 12:
                    var4 = "Unit";
                    break;
                case 20:
                    var4 = "Parser Symbol";
                    break;
                case 101:
                    var4 = "User defined argument";
                    break;
                case 102:
                    var4 = "User defined recursive argument";
                    break;
                case 103:
                    var4 = "User defined function";
                    break;
                case 104:
                    var4 = "User defined constant";
            }

            String var6 = getLeftSpaces("12345", Integer.toString(var7 + 1)) + ". " + getRightSpaces("01234567890123456789", var5) + getRightSpaces("                        ", "<" + var4 + ">") + getRightSpaces("0123456789012345678901234567890123456789012345", var8.syntax) + getRightSpaces("012345", var8.since) + var8.description + "\n";
            if (var6.toLowerCase().indexOf(var1.toLowerCase()) >= 0) {
                var2 = var2 + var6;
            }
        }

        return var2;
    }

    public List<KeyWord> getKeyWords() {
        return this.getKeyWords("");
    }

    public List<KeyWord> getKeyWords(String var1) {
        this.keyWordsList = new ArrayList();
        ArrayList var2 = new ArrayList();
        this.addParserKeyWords();
        this.validateParserKeyWords();
        if (!this.parserKeyWordsOnly) {
            this.addArgumentsKeyWords();
            this.addFunctionsKeyWords();
            this.addConstantsKeyWords();
        }

        Collections.sort(this.keyWordsList, new KwTypeComparator());
        Iterator var4 = this.keyWordsList.iterator();

        while(var4.hasNext()) {
            KeyWord var5 = (KeyWord)var4.next();
            String var3 = "str=" + var5.wordString + " desc=" + var5.description + " syn=" + var5.syntax + " sin=" + var5.since + " wid=" + var5.wordId + " tid=" + var5.wordTypeId;
            if (var3.toLowerCase().indexOf(var1.toLowerCase()) >= 0) {
                var2.add(var5);
            }
        }

        return var2;
    }

    void showTokens() {
        showTokens(this.tokensList);
    }

    static final void showTokens(List<Token> var0) {
        String var1 = "TokenTypeId";
        mXparser.consolePrintln(" --------------------");
        mXparser.consolePrintln("| Expression tokens: |");
        mXparser.consolePrintln(" ---------------------------------------------------------------------------------------------------------------");
        mXparser.consolePrintln("|    TokenIdx |       Token |        KeyW |     TokenId | TokenTypeId |  TokenLevel |  TokenValue |   LooksLike |");
        mXparser.consolePrintln(" ---------------------------------------------------------------------------------------------------------------");
        if (var0 == null) {
            mXparser.consolePrintln("NULL tokens list");
        } else {
            int var2 = var0.size();

            for(int var3 = 0; var3 < var2; ++var3) {
                String var4 = getLeftSpaces(var1, Integer.toString(var3));
                String var5 = getLeftSpaces(var1, ((Token)var0.get(var3)).tokenStr);
                String var6 = getLeftSpaces(var1, ((Token)var0.get(var3)).keyWord);
                String var7 = getLeftSpaces(var1, Integer.toString(((Token)var0.get(var3)).tokenId));
                String var8 = getLeftSpaces(var1, Integer.toString(((Token)var0.get(var3)).tokenTypeId));
                String var9 = getLeftSpaces(var1, Integer.toString(((Token)var0.get(var3)).tokenLevel));
                String var10 = getLeftSpaces(var1, Double.toString(((Token)var0.get(var3)).tokenValue));
                String var11 = getLeftSpaces(var1, ((Token)var0.get(var3)).looksLike);
                mXparser.consolePrintln("| " + var4 + " | " + var5 + " | " + var6 + " | " + var7 + " | " + var8 + " | " + var9 + " | " + var10 + " | " + var11 + " |");
            }

            mXparser.consolePrintln(" ---------------------------------------------------------------------------------------------------------------");
        }
    }

    void showInitialTokens() {
        showTokens(this.initialTokens);
    }

    private void showArguments() {
        Iterator var1 = this.argumentsList.iterator();

        while(var1.hasNext()) {
            Argument var2 = (Argument)var1.next();
            boolean var3 = var2.getVerboseMode();
            var2.setSilentMode();
            this.printSystemInfo(var2.getArgumentName() + " = " + var2.getArgumentValue() + "\n", true);
            if (var3) {
                var2.setVerboseMode();
            }
        }

    }

    private void printSystemInfo(String var1, boolean var2) {
        if (var2) {
            mXparser.consolePrint("[" + this.description + "][" + this.expressionString + "] " + var1);
        } else {
            mXparser.consolePrint(var1);
        }

    }

    protected Expression clone() {
        Expression var1 = new Expression(this);
        if (this.initialTokens != null && this.initialTokens.size() > 0) {
            var1.initialTokens = this.createInitialTokens(0, this.initialTokens.size() - 1, this.initialTokens);
        }

        return var1;
    }
}
