package com.pranavv.filevista.awsmicroservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value="user-auth-controller")
public interface Userproxy {

    @PutMapping("/signup-asNewUser/{username}/{fileid}/{filetype}/{filesize}")
    public String addFileData(@PathVariable String username,@PathVariable String fileid,@PathVariable String filetype,@PathVariable String filesize);
}
