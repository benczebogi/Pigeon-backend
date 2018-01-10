package me.kalmanbncz.api.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.kalmanbncz.domain.Contact;
import me.kalmanbncz.exception.DataFormatException;
import me.kalmanbncz.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/example/v1/contacts")
@Api(tags = {"contacts"})
public class ContactController extends AbstractRestHandler {

    @Autowired
    private ContactService contactService;

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a contact resource.", notes = "Returns the URL of the new resource in the Location header.")
    public void createContact(@RequestBody Contact contact,
                              HttpServletRequest request, HttpServletResponse response) {
        Contact createdContact = this.contactService.createContact(contact);
        response.setHeader("Location", request.getRequestURL().append("/").append(createdContact.getId()).toString());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all contacts.", notes = "The list is paginated. You can provide a page number (default 0) and a page size (default 100)")
    public
    @ResponseBody
    Page<Contact> getAllContact(@ApiParam(value = "The page number (zero-based)", required = true)
                                @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                @ApiParam(value = "Tha page size", required = true)
                                @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                HttpServletRequest request, HttpServletResponse response) {
        return this.contactService.getAllContacts(page, size);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single contact.", notes = "You have to provide a valid contact ID.")
    public
    @ResponseBody
    Contact getContact(@ApiParam(value = "The ID of the contact.", required = true)
                       @PathVariable("id") Long id,
                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        Contact contact = this.contactService.getContact(id);
        checkResourceFound(contact);
        //todo: http://goo.gl/6iNAkz
        return contact;
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update a contact resource.", notes = "You have to provide a valid contact ID in the URL and in the payload. The ID attribute can not be updated.")
    public void updateContact(@ApiParam(value = "The ID of the existing contact resource.", required = true)
                              @PathVariable("id") Long id, @RequestBody Contact contact,
                              HttpServletRequest request, HttpServletResponse response) {
        checkResourceFound(this.contactService.getContact(id));
        if (id != contact.getId()) throw new DataFormatException("ID doesn't match!");
        this.contactService.updateContact(contact);
    }

    //todo: @ApiImplicitParams, @ApiResponses
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a contact resource.", notes = "You have to provide a valid contact ID in the URL. Once deleted the resource can not be recovered.")
    public void deleteContact(@ApiParam(value = "The ID of the existing contact resource.", required = true)
                              @PathVariable("id") Long id, HttpServletRequest request,
                              HttpServletResponse response) {
        checkResourceFound(this.contactService.getContact(id));
        this.contactService.deleteContact(id);
    }
}
