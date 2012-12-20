package nl.han.ica.core.issue.solver.magicliteral;

import java.util.Map;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;

public class NumberLiteralSolutionBuilder extends LiteralSolutionBuilder {

	public NumberLiteralSolutionBuilder(Issue issue,
			IssueSolver issueSolver, Map<String, Parameter> parameters, String parameterConstantName) {
		super(issue, issueSolver, parameters, parameterConstantName);
	}

	@Override
	public Solution build() {

		String name = (String) parameters.get(parameterConstantName)
				.getValue();
		NumberLiteral numberLiteral = (NumberLiteral) literal;
		String value = numberLiteral.getToken();

		if (!existingConstantExists(name)) {
			createConstant(name, value);
		}
		replaceMagicLiteralWithConstant(name);
		return buildSolution();
	}

	@Override
	protected Type getType(AST ast) {

		NumberLiteral numberLiteral = (NumberLiteral) literal;
		String token = numberLiteral.getToken();

		if (token.contains("f")) {
			return ast.newPrimitiveType(PrimitiveType.FLOAT);
		} else if (token.contains(".")) {
			return ast.newPrimitiveType(PrimitiveType.DOUBLE);
		} else if (token.contains("L")) {
			return ast.newPrimitiveType(PrimitiveType.LONG);
		} else {
			return ast.newPrimitiveType(PrimitiveType.INT);
		}

	}

	@Override
	protected Expression getInitializerExpression(String value, AST ast) {

		return ast.newNumberLiteral(value);
	}

}
