package com.bfam.riskreport.ui

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class RiskReportView : View("Risk Report") {
    val model = ViewModel()
    val username = model.bind { SimpleStringProperty() }
    val password = model.bind { SimpleStringProperty() }
    val riskReportController: RiskReportController by inject()

    override val root = form {
        fieldset(labelPosition = Orientation.VERTICAL) {
            field("Username") {
                textfield(username).required()
            }
            field("Password") {
                passwordfield(password).required()
            }
            button("Log in") {
                enableWhen(model.valid)
                isDefaultButton = true
                useMaxWidth = true
                action {
                    runAsyncWithProgress {
                        riskReportController.login(username.value, password.value)
                    }
                }
            }
        }
        label(riskReportController.statusProperty) {
            style {
                paddingTop = 10
                textFill = Color.RED
                fontWeight = FontWeight.BOLD
            }
        }
    }

    override fun onDock() {
        username.value = ""
        password.value = ""
        model.clearDecorators()
    }
}