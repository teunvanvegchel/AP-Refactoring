package nl.han.ica.app.models.parameter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import nl.han.ica.core.strategies.solvers.Parameter;

/**
 * Creates form controls based on the parameter value type. Furthermore, it configures the control to synchronize its
 * value with the parameter and to trigger user-defined events.
 */
public class ParameterControlFactory {

    /**
     * Create a control for a parameter.
     * @param parameter The parameter to base the control on.
     * @return Control capable of displaying the parameter value.
     */
    public Control controlForParameter(final Parameter parameter) {
        return controlForParameter(parameter, null);
    }

    /**
     * Create a control for a parameter, and add a change event handler.
     * @param parameter The parameter to base the control on.
     * @param eventHandler Event handler that handles changes to the parameter value.
     * @return Control capable of displaying the parameter value.
     */
    public Control controlForParameter(final Parameter parameter, final EventHandler<ParameterEvent> eventHandler) {
        if (parameter.getValue().getClass() == String.class) {
            return createTextFieldControl(parameter, eventHandler);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + parameter.getValue().getClass());
        }
    }

    private TextField createTextFieldControl(final Parameter parameter, final EventHandler<ParameterEvent> eventHandler) {
        TextField textField = new TextField();
        textField.setText((String) parameter.getValue());
        textField.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent event) {
                parameter.setValue(((TextField) event.getSource()).getText());
                if (null != eventHandler) {
                    eventHandler.handle(new ParameterEvent(parameter));
                }
            }
        });
        return textField;
    }


}
