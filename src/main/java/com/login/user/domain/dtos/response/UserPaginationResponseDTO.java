package com.login.user.domain.dtos.response;

import java.util.List;

public record UserPaginationResponseDTO(long items, int pages, List<UserResponseDTO> users) {}
