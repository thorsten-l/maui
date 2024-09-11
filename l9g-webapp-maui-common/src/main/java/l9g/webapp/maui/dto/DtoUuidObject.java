package l9g.webapp.maui.dto;

//~--- non-JDK imports --------------------------------------------------------
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;

//~--- JDK imports ------------------------------------------------------------
import java.util.Date;

import l9g.webapp.maui.json.View;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Dr. Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DtoUuidObject
{  
  @JsonView(View.Admin.class)
  private String createdBy;

  @JsonView(View.Admin.class)
  private String modifiedBy;

  @JsonView(View.Admin.class)
  protected Date createTimestamp;

  @JsonView(View.Admin.class)
  protected Date modifyTimestamp;

  @JsonView(View.Base.class)
  private String id;
}
