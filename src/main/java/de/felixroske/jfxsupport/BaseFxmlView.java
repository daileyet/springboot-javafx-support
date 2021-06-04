package de.felixroske.jfxsupport;

import java.util.Optional;

/**
 * Base fxml view
 *
 * @author dailey.dai@openthinks.com
 */
public class BaseFxmlView extends AbstractFxmlView {

  private static final Optional<String> EMPTY_TITLE = Optional.empty();

  /**
   * get fxml title
   * @return title
   */
  public Optional<String> getTitle() {
    return "".equals(getDefaultTitle()) ? EMPTY_TITLE : Optional.of(getDefaultTitle());
  }
}