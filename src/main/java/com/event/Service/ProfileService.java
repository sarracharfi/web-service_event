package com.event.Service;

import com.event.Model.Profile;
import com.event.Model.User;
import com.event.Repository.ProfileRepository;
import com.event.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    @Autowired
    private  UserRepository userRepository;

    private final String uploadDir = "uploads/profile_pictures/";

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile createProfile(Profile profile, String username, MultipartFile profilePicture) throws IOException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        profile.setUser(user.get());


        String filePath = saveProfilePicture(profilePicture);

        profile.setProfilePicture(filePath);

        return profileRepository.save(profile);
    }

    public Profile updateProfile(Long id, Profile profile, MultipartFile profilePicture) throws IOException {
        Optional<Profile> existingProfile = profileRepository.findById(id);
        if (existingProfile.isPresent()) {
            Profile updatedProfile = existingProfile.get();

            updatedProfile.setFirstName(profile.getFirstName());
            updatedProfile.setLastName(profile.getLastName());
            updatedProfile.setBio(profile.getBio());

            if (profilePicture != null && !profilePicture.isEmpty()) {
                String filePath = saveProfilePicture(profilePicture);
                updatedProfile.setProfilePicture(filePath);
            }

            return profileRepository.save(updatedProfile);
        }

        throw new RuntimeException("Profile not found");
    }

    public Optional<Profile> getProfile(Long id) {
        return profileRepository.findById(id);
    }

    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }

    private String saveProfilePicture(MultipartFile file) throws IOException {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = System.currentTimeMillis() + "_" + originalFileName;
        Path path = Paths.get(uploadDir + fileName);

        Files.copy(file.getInputStream(), path);

        return fileName;
    }
}
