package de.felixroske.jfxsupport;

import java.util.function.Consumer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Abstract base view
 *
 * @author dailey.dai@openthinks.com
 */
public abstract class AbstractView implements ApplicationContextAware {

  /**
   * Spring Context
   */
  protected ApplicationContext applicationContext;
  /**
   * property of controller
   */
  protected final ObjectProperty<Object> presenterProperty;

  protected AbstractView() {
    this.presenterProperty = new SimpleObjectProperty<>();
  }


  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    if (this.applicationContext != null) {
      return;
    }
    this.applicationContext = applicationContext;
  }

  public Object getPresenter() {
    return presenterProperty.get();
  }

  /**
   * Creates the controller for type.
   *
   * @param type the type
   * @return the object
   */
  protected final Object createControllerForType(final Class<?> type) {
    return applicationContext.getBean(type);
  }

  /**
   * Does not initialize the view. Only registers the Consumer and waits until the the view is going
   * to be created / the method FXMLView#getView or FXMLView#getViewAsync invoked.
   *
   * @param presenterConsumer listener for the presenter construction
   */
  public void getPresenter(final Consumer<Object> presenterConsumer) {

    presenterProperty.addListener(
        (final ObservableValue<? extends Object> o, final Object oldValue, final Object newValue) -> presenterConsumer
            .accept(newValue));
  }

  public abstract Parent getView();
}