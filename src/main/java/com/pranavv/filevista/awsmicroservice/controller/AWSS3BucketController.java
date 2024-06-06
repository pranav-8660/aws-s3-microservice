package com.pranavv.filevista.awsmicroservice.controller;

import com.pranavv.filevista.awsmicroservice.service.AWSServiceClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value="aws-client-handler/")
@CrossOrigin
public class AWSS3BucketController {

    // http://localhost:5000/aws-client-handler/addFile/folderName/{folName}
    // http://localhost:5000/aws-client-handler/create-five-folders/userName/{username}
    // http://localhost:5000/aws-client-handler/add-proper-file-to-folder/tagNumber/{tagno}/userName/{username}
    // http://localhost:5000/aws-client-handler/fetch-files/fromFolder/{foldername}/forUser/{username}
    // http://localhost:5000/aws-client-handler/delete-from-folder
    private final AWSServiceClass serviceInst;

    public AWSS3BucketController(AWSServiceClass serviceInst) {
        this.serviceInst = serviceInst;
    }

    @PutMapping(value="addFile/folderName/{folName}")
    public String addFile(@PathVariable("folName") String folName,@RequestParam("fileInstance") MultipartFile fileInst){
        return serviceInst.insertFileOntoFolder(folName,fileInst);
    }

    @GetMapping(value="create-five-folders/userName/{username}")
    public String createFirstFiveFolders(@PathVariable("username") String username){

        if(serviceInst.createFiveFolders(username))
            return "Success";
        return "failure";

    }

    @CrossOrigin
    @PostMapping(value="add-proper-file-to-folder/tagNumber/{tagno}/userName/{username}")
    public String addproperfiletoFolder(@PathVariable("tagno") int tagno,@PathVariable("username") String username,@RequestParam("mfileinst") MultipartFile fileInst){

        boolean res = serviceInst.validator(tagno,fileInst);

        if(res){
            return serviceInst.addfilevalidatedintoFolder(tagno,username,fileInst);
        }

        return "Irregular format selected!";

        //serviceInst
    }


    @PutMapping(value="add-scratch-file/{username}")
    public String addscratchFile(@PathVariable String username,@RequestBody byte[] fileArray)  {
        try {
            return serviceInst.addScratchFileintoFolder(username,fileArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value="fetch-files/fromFolder/{foldername}/forUser/{username}")
    public ResponseEntity<List<String>> fetchFilesforuserfromFolder(@PathVariable String username,@PathVariable String foldername){
        return ResponseEntity.ok(serviceInst.FetchfilefromFolderforuser(username,foldername));
    }

    @DeleteMapping(value="/delete-from-folder")
    public String deleteFromFolderforuser(@RequestBody List<String> urlList){
       return serviceInst.deleteaFilefromFolder(urlList);
    }

}
