package de.felixroske.jfxsupport;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import javafx.beans.property.Property;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Base controller
 *
 * @author dailey.dai@openthinks.com
 */
public class BaseController {

  public static final String METHOD_INITIALIZE_NAME = "initialize";
  public static final String PROPERTY_RESOURCES_NAME = "resources";
  @FXML
  protected URL location;
  @FXML
  protected ResourceBundle resources;

  public BaseController() {
  }

  /**
   * call when View loaded
   */
  public void initialize() {
  }

  public void setLocation(URL location) {
    this.location = location;
  }

  public void setResources(ResourceBundle resources) {
    this.resources = resources;
  }
}