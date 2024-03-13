package com.ntt.crud.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationConfiguration configuration;

    @Test
    void modelMapper() {
        applicationConfig.modelMapper();
    }

    @Test
    void authenticationManager() throws Exception {
        applicationConfig.authenticationManager(configuration);
    }

    @Test
    void authenticationProvider() {
        applicationConfig.authenticationProvider();
    }

}