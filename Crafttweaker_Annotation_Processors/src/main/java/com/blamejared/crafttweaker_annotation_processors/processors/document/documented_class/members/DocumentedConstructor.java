package com.blamejared.crafttweaker_annotation_processors.processors.document.documented_class.members;

import com.blamejared.crafttweaker_annotation_processors.processors.document.documented_class.DocumentedClass;
import com.blamejared.crafttweaker_annotation_processors.processors.document.shared.CommentUtils;
import com.blamejared.crafttweaker_annotation_processors.processors.document.shared.Writable;
import com.blamejared.crafttweaker_annotation_processors.processors.document.shared.members.DocumentedParameter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DocumentedConstructor implements Writable {

    public static final Comparator<? super DocumentedConstructor> compareByParameterCount = Comparator.comparingInt(e -> e.parameterList
            .size());


    private final List<DocumentedParameter> parameterList;
    private final DocumentedClass containingClass;
    private final String docComment;

    public DocumentedConstructor(DocumentedClass containingClass, List<DocumentedParameter> parameterList, String docComment) {
        this.containingClass = containingClass;
        this.parameterList = parameterList;
        this.docComment = docComment;
    }

    public static DocumentedConstructor fromConstructor(DocumentedClass containingClass, ExecutableElement method, ProcessingEnvironment environment) {
        if (method.getKind() != ElementKind.CONSTRUCTOR) {
            environment.getMessager()
                    .printMessage(Diagnostic.Kind.ERROR, "Internal Error: Expected this to be a construcor", method);
            return null;
        }

        final List<DocumentedParameter> parameters = new ArrayList<>();
        for (VariableElement parameter : method.getParameters()) {
            parameters.add(DocumentedParameter.fromElement(parameter, environment));
        }
        final String docComment = CommentUtils.formatDocCommentForDisplay(method, environment);
        return new DocumentedConstructor(containingClass, parameters, docComment);
    }

    @Override
    public void write(PrintWriter writer) {
        if (docComment != null) {
            writer.println(docComment);
        }

        writer.println("```zenscript");
        DocumentedParameter.printAllCalls("new " + containingClass.getZSName(), parameterList, writer);
        writer.println("```");

        if (!parameterList.isEmpty()) {
            DocumentedParameter.printTable(parameterList, writer);
            writer.println();
        }
    }
}
