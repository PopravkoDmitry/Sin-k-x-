package com.example.views;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CoefficientsView extends HBox {
    private final TextField aTextField;
    private final TextField kTextField;
    private final TextField stepTextField;

    public CoefficientsView() {
        this.aTextField = new TextField("50");
        this.kTextField = new TextField("300");
        this.stepTextField = new TextField("1");
        Text aText = new Text("A: ");
        Text kText = new Text("K: ");
        Text stepText = new Text("Step: ");
        getChildren().addAll(aText, this.aTextField, stepText, this.stepTextField, kText, this.kTextField);
    }

    public double getA() {
        return Double.parseDouble(this.aTextField.getText());
    }

    public double getK() {
        return Double.parseDouble(this.kTextField.getText());
    }

    public double getStep() {
        return Double.parseDouble(this.stepTextField.getText());
    }
}
