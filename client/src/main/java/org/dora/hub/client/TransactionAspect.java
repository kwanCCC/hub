package org.dora.hub.client;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.FieldInfo;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.dora.hub.client.annotation.TransactionStart;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder.Exclude;

/**
 * Created by <-chan 666 on 2018/6/12.
 */
public class TransactionAspect {

    private static final String METHOD_TEMPLATE;

    static {
        /**
         * transactionStartProcessor preIntercept method body
         * param (String globalTxId,int timeout,int retries)
         *
         * transactionStartProcessor postIntercept method body
         * param (String globalTxID)
         */
        METHOD_TEMPLATE = "{ transactionStartProcessor.preIntercept(%s,%d,%d);\n" +
            "  $_ = $proceed($$);\n" +
            "  transactionStartProcessor.postIntercept(%s); }";

    }

    private final Reflections reflections = new Reflections(new ConfigurationBuilder().addScanners(
        new MethodAnnotationsScanner()).filterInputsBy(
        new Exclude("org.dora.hub.*")));

    private final ClassPool classPool = ClassPool.getDefault();

    private final TransactionStartProcessor processor;

    private final TransactionContext context;

    public TransactionAspect(TransactionStartProcessor processor, TransactionContext context) {
        this.processor = processor;
        this.context = context;
    }

    public void init() throws NotFoundException, IOException, CannotCompileException, ClassNotFoundException {
        Set<Method> transactionStartMtds = reflections.getMethodsAnnotatedWith(TransactionStart.class);
        for (Method m : transactionStartMtds) {
            Class<?> declaringClass = m.getDeclaringClass();
            CtClass ctClass = classPool.get(declaringClass.getName());
            ctClass.getConstructors();
            // add transactionStartProcessor field
            CtField ctField = new CtField(classPool.get(this.processor.getClass().getName()),
                "transactionStartProcessor", ctClass);
            ctField.setModifiers(Modifier.PRIVATE | Modifier.FINAL);

            FieldInfo f = new FieldInfo(ctClass.getClassFile().getConstPool(), "width", "I");

            new CtField()
            ctClass.addField(ctField, );
            ctClass.getClassFile().addField();

            String originMthName = m.getName();
            CtMethod originTransactionMethod = ctClass.getDeclaredMethod(originMthName);
            TransactionStart annotation = (TransactionStart)originTransactionMethod.getAnnotation(
                TransactionStart.class);
            int timeout = annotation.timeout();

            originTransactionMethod.instrument(new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {

                    String methodBody = String.format(METHOD_TEMPLATE, "globalTxId", "timeout", "retries",
                        "globalTxID");
                    m.replace(methodBody);
                }
            });
            //String newName = originMthName + "_origin";
            //originTransactionMethod.setName(newName);
            //
            //CtClass returnType = originTransactionMethod.getReturnType();
            //CtMethod insteadTxMtd = CtNewMethod.copy(originTransactionMethod, originMthName, ctClass, null);
            //StringBuilder sb = new StringBuilder();
            //
            //sb.append("processor.preIntercept(").append(")")
            //    .append(returnType.getClass().getName())
            //    .append(" ")
            //    .append(newName)
            //    .append("($$);\n")
            //    .append("processor.");
            //ctClass.writeFile();
        }
    }
}
