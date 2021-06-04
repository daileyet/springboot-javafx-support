package de.felixroske.jfxsupport;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract code view, no reference fxml file
 *
 * @author dailey.dai@openthinks.com
 */
public abstract class AbstractCodeView extends AbstractView {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFxmlView.class);
  /**
   * root node of this view
   */
  protected Parent root;
  protected ResourceBundle resourceBundle;

  @Override
  public Parent getView() {
    if (root == null) {
      root = createView();
    }
    ensureViewInitialized();
    return root;
  }

  /**
   * make sure view was initialized before used
   */
  private void ensureViewInitialized() {
    if (root != null) {
      return;
    }
    Exception ex = null;
    try {
      root = createView();
    } catch (Exception e) {
      ex = e;
    }
    if (root == null) {
      if (ex == null) {
        LOGGER.warn("Failed to create view by code");
      } else {
        LOGGER.warn("Failed to create view by code", ex);
      }
      return;
    }
    final Optional<Class<?>> optionalClass = controllerClass();
    if (optionalClass == null || !optionalClass.isPresent()) {
      LOGGER.debug("Not set reference controller class.");
      return;
    }

    optionalClass.ifPresent(clazz -> {
      getPresenter(PRESENT_CONSUMER);
      try {
        Object obj = createControllerForType(clazz);
        presenterProperty.set(obj);
      } catch (Exception e) {
        LOGGER.warn("Failed to create controller by type:{} for reason:{}", clazz, e);
      }

    });
  }

  /**
   * callback after reference controller of this view be set
   */
  protected final Consumer<Object> PRESENT_CONSUMER = (ctrl) -> {
    if (ctrl == null) {
      return;
    }
    // call method initialize
    try {
      callControllerInitializedMethod(ctrl);
    } catch (Exception e) {
      LOGGER.warn("Failed to call controller initialize method for reason:{}", e);
    }
  };

  /**
   * call initialize method in reference controller
   * @param controller
   * @throws Exception
   */
  protected void callControllerInitializedMethod(Object controller) throws Exception {
    if (controller instanceof BaseController) {
      ((BaseController) controller).setResources(resourceBundle);
      ((BaseController) controller).initialize();
      return;
    }
    if (controller instanceof Initializable) {
      ((Initializable) controller).initialize(null, resourceBundle);
      return;
    }
    Field resourcesField = controllerClass().get()
        .getDeclaredField(BaseController.PROPERTY_RESOURCES_NAME);
    if (resourcesField != null && ResourceBundle.class.isAssignableFrom(resourcesField.getType())) {
      resourcesField.set(controller, resourceBundle);
    }
    Method initializeMethod = controllerClass().get()
        .getDeclaredMethod(BaseController.METHOD_INITIALIZE_NAME);
    if (initializeMethod != null) {
      initializeMethod.setAccessible(true);
      initializeMethod.invoke(controller);
    }
  }

  /**
   * set resourceBundle
   * @param resourceBundle {@link ResourceBundle}
   */
  public void setResourceBundle(ResourceBundle resourceBundle) {
    this.resourceBundle = resourceBundle;
  }

  /**
   * create view by code
   *
   * @return root node
   */
  protected abstract Parent createView();

  /**
   * reference controller class type, will be call after
   *
   * @return
   */
  protected abstract Optional<Class<?>> controllerClass();
}
