package com.milkmate.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.milkmate.entity.Farmer;
import com.milkmate.entity.Role;
import com.milkmate.entity.User;
import com.milkmate.exception.ResourceNotFoundException;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.repository.UserRepository;
import com.milkmate.service.FarmerService;

@Service
public class FarmerServiceImpl implements FarmerService {

    private static final String DEFAULT_FARMER_PASSWORD = "MilkMate@123";

    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public FarmerServiceImpl(
            FarmerRepository farmerRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.farmerRepository = farmerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Farmer createFarmer(Farmer farmer) {

        if (farmerRepository.existsByFarmerCode(farmer.getFarmerCode())) {
            throw new IllegalArgumentException(
                    "Farmer code already exists");
        }

        if (farmerRepository.existsByMobile(farmer.getMobile())) {
            throw new IllegalArgumentException(
                    "Mobile number already exists");
        }

        if (userRepository.existsByMobile(farmer.getMobile())) {
            throw new IllegalArgumentException(
                    "This mobile number already has a login account");
        }

        if (farmer.getActive() == null) {
            farmer.setActive(true);
        }

        Farmer savedFarmer = farmerRepository.save(farmer);

        createFarmerLoginAccount(
                savedFarmer,
                DEFAULT_FARMER_PASSWORD
        );

        return savedFarmer;
    }

    private void createFarmerLoginAccount(
            Farmer farmer,
            String rawPassword) {

        User user = new User();

        user.setFullName(farmer.getFullName());
        user.setMobile(farmer.getMobile());
        user.setEmail(null);

        user.setPassword(
                passwordEncoder.encode(rawPassword)
        );

        user.setRole(Role.FARMER);

        user.setActive(
                Boolean.TRUE.equals(farmer.getActive())
        );

        userRepository.save(user);
    }

    @Override
    public Farmer getFarmerById(Long id) {

        return farmerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Farmer not found with id: " + id));
    }

    @Override
    public Farmer getFarmerByCode(String farmerCode) {

        return farmerRepository
                .findByFarmerCode(farmerCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Farmer not found with code: "
                                        + farmerCode));
    }

    @Override
    public Farmer getFarmerByMobile(String mobile) {

        return farmerRepository
                .findByMobile(mobile)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Farmer not found with mobile: "
                                        + mobile));
    }

    @Override
    public List<Farmer> getAllFarmers() {

        return farmerRepository.findAll();
    }

    @Override
    @Transactional
    public Farmer updateFarmer(Long id, Farmer farmer) {

        Farmer existingFarmer =
                farmerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Farmer not found with id: "
                                                + id));

        String oldMobile = existingFarmer.getMobile();

        User linkedUser =
                userRepository.findByMobile(oldMobile)
                        .orElse(null);

        if (!oldMobile.equals(farmer.getMobile())) {

            if (farmerRepository.existsByMobile(
                    farmer.getMobile())) {

                throw new IllegalArgumentException(
                        "Mobile number already exists");
            }

            User anotherUser =
                    userRepository
                            .findByMobile(farmer.getMobile())
                            .orElse(null);

            if (anotherUser != null
                    && anotherUser != linkedUser) {

                throw new IllegalArgumentException(
                        "This mobile number already has a login account");
            }
        }

        existingFarmer.setFullName(
                farmer.getFullName());

        existingFarmer.setMobile(
                farmer.getMobile());

        existingFarmer.setVillage(
                farmer.getVillage());

        existingFarmer.setAddress(
                farmer.getAddress());

        if (farmer.getActive() != null) {

            existingFarmer.setActive(
                    farmer.getActive());
        }

        Farmer updatedFarmer =
                farmerRepository.save(existingFarmer);

        if (linkedUser == null) {

            createFarmerLoginAccount(
                    updatedFarmer,
                    DEFAULT_FARMER_PASSWORD
            );

        } else {

            linkedUser.setFullName(
                    updatedFarmer.getFullName());

            linkedUser.setMobile(
                    updatedFarmer.getMobile());

            linkedUser.setRole(Role.FARMER);

            linkedUser.setActive(
                    Boolean.TRUE.equals(
                            updatedFarmer.getActive())
            );

            userRepository.save(linkedUser);
        }

        return updatedFarmer;
    }

    @Override
    @Transactional
    public void deleteFarmer(Long id) {

        Farmer farmer =
                farmerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Farmer not found with id: "
                                                + id));

        farmer.setActive(false);

        farmerRepository.save(farmer);

        User user =
                userRepository
                        .findByMobile(farmer.getMobile())
                        .orElse(null);

        if (user != null) {

            user.setActive(false);

            userRepository.save(user);
        }
    }
}