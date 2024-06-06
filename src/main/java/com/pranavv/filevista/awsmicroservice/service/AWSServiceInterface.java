package com.pranavv.filevista.awsmicroservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface AWSServiceInterface {
    public String insertFileOntoFolder(String folder, MultipartFile fileInst);
    public boolean createFiveFolders(String userName);
}
