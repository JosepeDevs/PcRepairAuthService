package com.josepdevs.Infra.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@RequestMapping("api/v1/admin/")
@RestController
@Hidden //this avoids OpenAPI /Swagger to map this controller
public class UpdateRoleController {

}
