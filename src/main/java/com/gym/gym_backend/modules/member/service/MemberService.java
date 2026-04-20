package com.gym.gym_backend.modules.member.service;

import com.gym.gym_backend.common.exception.EmailAlreadyExistsException;
import com.gym.gym_backend.common.exception.ResourceNotFoundException;
import com.gym.gym_backend.modules.member.dto.MemberDto;
import com.gym.gym_backend.modules.member.entity.Member;
import com.gym.gym_backend.modules.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberDto.Response createMember(UUID tenantId, UUID gymId, MemberDto.CreateRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        Member member = new Member();
        member.setTenantId(tenantId);
        member.setGymId(gymId);
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setAddress(request.getAddress());
        member.setDateOfBirth(request.getDateOfBirth());
        member.setGender(request.getGender());
        member.setPackageId(request.getPackageId());
        member.setMembershipStartDate(LocalDate.now());
        member.setStatus(Member.MembershipStatus.ACTIVE);
        member.setEmergencyContactName(request.getEmergencyContactName());
        member.setEmergencyContactPhone(request.getEmergencyContactPhone());
        member.setNotes(request.getNotes());

        member = memberRepository.save(member);
        return mapToResponse(member);
    }

    public MemberDto.Response getMember(UUID tenantId, UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        if (!member.getTenantId().equals(tenantId)) {
            throw new ResourceNotFoundException("Unauthorized");
        }

        return mapToResponse(member);
    }

    public List<MemberDto.Response> getGymMembers(UUID tenantId, UUID gymId) {
        return memberRepository.findByTenantIdAndGymId(tenantId, gymId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MemberDto.Response> getTenantMembers(UUID tenantId) {
        return memberRepository.findByTenantId(tenantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberDto.Response updateMember(UUID tenantId, UUID memberId, MemberDto.UpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        if (!member.getTenantId().equals(tenantId)) {
            throw new ResourceNotFoundException("Unauthorized");
        }

        if (request.getFirstName() != null) member.setFirstName(request.getFirstName());
        if (request.getLastName() != null) member.setLastName(request.getLastName());
        if (request.getPhone() != null) member.setPhone(request.getPhone());
        if (request.getAddress() != null) member.setAddress(request.getAddress());
        if (request.getGender() != null) member.setGender(request.getGender());
        if (request.getPackageId() != null) member.setPackageId(request.getPackageId());
        if (request.getStatus() != null) {
            member.setStatus(Member.MembershipStatus.valueOf(request.getStatus()));
        }
        if (request.getEmergencyContactName() != null) member.setEmergencyContactName(request.getEmergencyContactName());
        if (request.getEmergencyContactPhone() != null) member.setEmergencyContactPhone(request.getEmergencyContactPhone());
        if (request.getNotes() != null) member.setNotes(request.getNotes());

        member = memberRepository.save(member);
        return mapToResponse(member);
    }

    @Transactional
    public void deleteMember(UUID tenantId, UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        if (!member.getTenantId().equals(tenantId)) {
            throw new ResourceNotFoundException("Unauthorized");
        }

        memberRepository.delete(member);
    }

    private MemberDto.Response mapToResponse(Member member) {
        return MemberDto.Response.builder()
                .id(member.getId())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .address(member.getAddress())
                .dateOfBirth(member.getDateOfBirth())
                .gender(member.getGender())
                .membershipStartDate(member.getMembershipStartDate())
                .membershipEndDate(member.getMembershipEndDate())
                .packageId(member.getPackageId())
                .status(member.getStatus().toString())
                .emergencyContactName(member.getEmergencyContactName())
                .emergencyContactPhone(member.getEmergencyContactPhone())
                .profilePhotoUrl(member.getProfilePhotoUrl())
                .notes(member.getNotes())
                .build();
    }
}
