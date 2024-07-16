package com.neobis.cookscorner.controller;

import com.neobis.cookscorner.dto.user.UserListOutDto;
import com.neobis.cookscorner.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get a filtered list of users",
            description = "Returns a filtered list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users successfully retrieved")
    })
    @GetMapping
    public ResponseEntity<Page<UserListOutDto>> getUsers(
            @RequestParam(required = false) String searchTerm,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getUsers(searchTerm, pageable));
    }

}