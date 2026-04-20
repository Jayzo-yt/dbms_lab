package com.gym.gym_backend.modules.member.controller;

import com.gym.gym_backend.common.dto.GlobalResponseDTO;
import com.gym.gym_backend.modules.member.dto.MemberDto;
import com.gym.gym_backend.modules.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/staff/gym/{gymId}/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<GlobalResponseDTO<MemberDto.Response>> createMember(
            @PathVariable UUID gymId,
            @Valid @RequestBody MemberDto.CreateRequest request,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        MemberDto.Response response = memberService.createMember(tenantId, gymId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponseDTO.success(response, "Member created successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<List<MemberDto.Response>>> getGymMembers(
            @PathVariable UUID gymId,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        List<MemberDto.Response> response = memberService.getGymMembers(tenantId, gymId);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Members retrieved successfully"));
    }

    @GetMapping("/{memberId}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<MemberDto.Response>> getMember(
            @PathVariable UUID gymId,
            @PathVariable UUID memberId,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        MemberDto.Response response = memberService.getMember(tenantId, memberId);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Member details retrieved"));
    }

    @PutMapping("/{memberId}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<MemberDto.Response>> updateMember(
            @PathVariable UUID gymId,
            @PathVariable UUID memberId,
            @Valid @RequestBody MemberDto.UpdateRequest request,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        MemberDto.Response response = memberService.updateMember(tenantId, memberId, request);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Member updated successfully"));
    }

    @DeleteMapping("/{memberId}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<Void>> deleteMember(
            @PathVariable UUID gymId,
            @PathVariable UUID memberId,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        memberService.deleteMember(tenantId, memberId);
        return ResponseEntity.ok(GlobalResponseDTO.success(null, "Member deleted successfully"));
    }
}
