package tk.djandjiev.practice.controller;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.djandjiev.practice.service.office.OfficeService;
import tk.djandjiev.practice.util.View;
import tk.djandjiev.practice.to.message.DataMessage;
import tk.djandjiev.practice.to.message.SuccessMessage;
import tk.djandjiev.practice.to.office.OfficeRequest;
import tk.djandjiev.practice.to.office.OfficeTO;
import tk.djandjiev.practice.to.office.SimplifiedOfficeTO;

@RestController
@RequestMapping(
    value = OfficeController.OFFICE_URL,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class OfficeController {

  private static final Logger log = getLogger(OfficeController.class);

  public static final String OFFICE_URL = "/office";

  @Autowired
  private OfficeService service;

  @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
  public DataMessage<List<SimplifiedOfficeTO>> getAll(@Valid @RequestBody OfficeRequest request) {
    log.info("get all offices of organization with id: {}.", request.getOrgId());
    List<SimplifiedOfficeTO> data = service.getAll(request);

    return new DataMessage<>(data);
  }

  @GetMapping(value = "/{id}")
  public DataMessage<OfficeTO> get(@PathVariable("id") Integer id) {
    log.info("get office with id: {}.", id);
    OfficeTO data = service.get(id);
    if (data == null) {
      throw new IllegalArgumentException("office with id: " + id + " not found");
    }

    return new DataMessage<>(data);
  }

  @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
  public SuccessMessage update(@Validated(View.Updateable.class) @RequestBody OfficeTO officeTO) {
    log.info("Update office with id: {}.", officeTO.getId());
    if (officeTO.getId() == null || officeTO.getName() == null || officeTO.getAddress() == null) {
      throw new IllegalArgumentException("required parameters are null.");
    }
    service.save(officeTO);

    return new SuccessMessage();
  }

  @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
  public SuccessMessage save(@Validated(View.Saveable.class) @RequestBody OfficeTO officeTO) {
    log.info("Create new office of organization with id: {}.");
    if (officeTO.getOrgId() == null) {
      throw new IllegalArgumentException("organization id can not be null.");
    }

    service.save(officeTO);

    return new SuccessMessage();
  }
}
