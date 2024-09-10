package l9g.webapp.maui.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Dr. Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Controller
@Slf4j
public class ErrorPageController implements ErrorController
{

  public ErrorPageController()
  {
    log.debug("ErrorPageController");
  }

  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, Model model)
  {
    // ControllerUtil.addGeneralAttributes(model, null, null);

    log.debug("handleError");
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    if (status != null)
    {
      Integer statusCode = Integer.valueOf(status.toString());

      model.addAttribute("pageErrorException", request.getAttribute(
        RequestDispatcher.ERROR_EXCEPTION));
      model.addAttribute("pageErrorExceptionType", request.getAttribute(
        RequestDispatcher.ERROR_EXCEPTION_TYPE));
      model.addAttribute("pageErrorMessage", request.getAttribute(
        RequestDispatcher.ERROR_MESSAGE));
      model.addAttribute("pageErrorRequestUri", request.getAttribute(
        RequestDispatcher.ERROR_REQUEST_URI));
      model.addAttribute("pageErrorServletName", request.getAttribute(
        RequestDispatcher.ERROR_SERVLET_NAME));
      model.addAttribute("pageErrorStatusCode", request.getAttribute(
        RequestDispatcher.ERROR_STATUS_CODE));

      if (statusCode == HttpStatus.NOT_FOUND.value())
      {
        return "error/404";
      }
      else if (statusCode == HttpStatus.FORBIDDEN.value())
      {
        return "error/403";
      }
      else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
      {
        return "error/500";
      }
    }

    return "error/error";
  }

  public String getErrorPath()
  {
    return "/error";
  }
}
