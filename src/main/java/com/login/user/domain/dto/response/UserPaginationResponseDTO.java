package com.login.user.domain.dto.response;

import java.util.List;

public record UserPaginationResponseDTO(long items, int pages, List<UserResponseDTO> users) {}
