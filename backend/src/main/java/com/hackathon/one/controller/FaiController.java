package com.hackathon.one.controller;

import com.hackathon.one.dto.FaiChatRequest;
import com.hackathon.one.dto.FaiChatResponse;
import com.hackathon.one.service.FaiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fai")
@RequiredArgsConstructor
@Tag(name = "Fai", description = "Assistente de finanças pessoais")
public class FaiController {

    private final FaiService faiService;

    @Operation(summary = "Conversar com Fai", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/chat")
    public ResponseEntity<FaiChatResponse> chat(
            @Valid @RequestBody FaiChatRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(faiService.responder(request, userDetails.getUsername()));
    }
}
