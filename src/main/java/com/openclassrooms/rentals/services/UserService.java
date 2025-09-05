package com.openclassrooms.rentals.services;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.openclassrooms.rentals.exceptions.UserNotFoundException;
import com.openclassrooms.rentals.dto.UserDto;
import com.openclassrooms.rentals.dto.UserProfileResponse;
import com.openclassrooms.rentals.exceptions.EmailAlreadyUsedException;
import com.openclassrooms.rentals.exceptions.InvalidUserDetailsException;
import com.openclassrooms.rentals.exceptions.InvalidUserProfileException;
import com.openclassrooms.rentals.models.AppUserDetails;
import com.openclassrooms.rentals.models.UserEntity;
import com.openclassrooms.rentals.repositorys.UserRepository;
import com.openclassrooms.rentals.services.map.UserMapper;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }
     public AppUserDetails createUser(String email, String password, String name) {

        if (isEmailAlreadyUsed(email)) {
            throw new EmailAlreadyUsedException("Email '" + email + "' is already present in database.",
                    "DefaultUserManagementService.createUser");
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return new AppUserDetails(user);
    }

    public UserDetails getUserbyEmail(String email) {
        return this.userRepository.findByEmail(email).map((user) -> new AppUserDetails(user))
                .orElseThrow(() -> new UserNotFoundException("User with " + email + " not found",
                        "UserService.getUserbyEmail"));

    }

    public Optional<Long> getUserId(String email) {
        return this.userRepository.findByEmail(email)
                .map(user -> user.getId());
    }
    public UserProfileResponse getUserProfilebyEmail(String email) {
    UserDetails userDetails = getUserbyEmail(email);

    if (!(userDetails instanceof AppUserDetails)) {
        throw new InvalidUserDetailsException("UserDetails is not of the expected type",
                "DefaultUserManagementService.getUserProfile");
    }

    AppUserDetails appUserDetails = (AppUserDetails) userDetails;
    return userMapper.toProfileResponse(appUserDetails.getUserEntity());
}

    public boolean isEmailAlreadyUsed(String email) {

        Optional<Long> optionalUserId = getUserId(email);

        return optionalUserId.isPresent();
    }

    public Optional<UserEntity> getUserEntityById(Long userId) {
        return userRepository.findById(userId);

    }

    public Optional<UserEntity> getUserEntityByMail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserProfileResponse getUserProfilebyId(Long userId) {

        Optional<UserEntity> optionalUserEntity = getUserEntityById(userId);

        // check if empty
        if (optionalUserEntity.isEmpty())
            throw new UserNotFoundException("User with id " + userId + "not found",
                    "DefaultUserManagementService.getUserEntityByMail");

        UserEntity user = optionalUserEntity.get();
        // check if email field is set
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new InvalidUserProfileException("User with ID " + userId + " has an invalid email.",
                    "DefaultUserManagementService.getUserProfilebyId");
        }

        return getUserProfilebyEmail(user.getEmail());
    }

    public Optional<UserEntity> getCurrentUserEntity() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof AppUserDetails) {
            AppUserDetails userDetails = (AppUserDetails) principal;
            return userRepository.findById(userDetails.getId());
        }

        return Optional.empty();
    }

	@Transactional(readOnly = true)
	public Optional<UserDto> findById(Long id) {
		return userRepository.findById(id).map(u -> userMapper.toDto(u));
	}
}