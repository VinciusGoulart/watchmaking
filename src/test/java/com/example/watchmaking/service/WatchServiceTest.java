package com.example.watchmaking.service;

import com.example.watchmaking.repository.BrandRepository;
import com.example.watchmaking.repository.WatchRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class WatchServiceTest {

    @Mock
    private WatchRepository watchRepository;

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private WatchService watchServiceMock;



}