package com.jecfalo.palermus_api.modules.users.controllers;

import com.jecfalo.palermus_api.modules.users.records.profile.ReferenceProfile;
import com.jecfalo.palermus_api.modules.users.records.profile.UpdateProfile;
import com.jecfalo.palermus_api.modules.users.records.profile.UpdateRole;
import com.jecfalo.palermus_api.modules.users.services.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    @Autowired
    private IProfileService service;

    @GetMapping("/{profileId}")
    public ResponseEntity<ReferenceProfile> getProfileId(@PathVariable Long profileId){
        ReferenceProfile reference = service.getProfileById(profileId);
        return ResponseEntity.ok(reference);
    }

    @GetMapping("/document/{document}")
    public ResponseEntity<ReferenceProfile> getProfileDocument(@PathVariable String document){
        ReferenceProfile reference = service.getProfileDocument(document);
        return ResponseEntity.ok(reference);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ReferenceProfile> getProfileUsername(@PathVariable String username){
        ReferenceProfile reference = service.getProfileByUsername(username);
        return ResponseEntity.ok(reference);
    }

    @PutMapping("/email/{profileId}")
    public ResponseEntity<ReferenceProfile> updateProfileEmail(@PathVariable Long profileId, @RequestBody UpdateProfile email){
        ReferenceProfile reference = service.updateProfileEmail(profileId, email);
        return ResponseEntity.ok(reference);
    }
    @PutMapping("/username/{profileId}")
    public ResponseEntity<ReferenceProfile> updateProfileUsername(@PathVariable Long profileId, @RequestBody UpdateProfile username){
        ReferenceProfile reference = service.updateProfileUsername(profileId, username);
        return ResponseEntity.ok(reference);
    }
    @PutMapping("/palermuspass/{profileId}")
    public ResponseEntity<Map<String, String>> updateProfilePassword(@PathVariable Long profileId, @RequestBody UpdateProfile password){
        String message = service.updateProfilePassword(profileId, password);
        return ResponseEntity.ok(Map.of("message", message));
    }
    @PutMapping("/palermusrole/{profileId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ReferenceProfile> updateProfileRole(@PathVariable Long profileId, @RequestBody UpdateRole role){
        ReferenceProfile reference = service.updateProfileRole(profileId, role);
        return ResponseEntity.ok(reference);
    }
    @DeleteMapping("/{profileId}")
    public ResponseEntity<Map<String, String>> deleteProfile(@PathVariable Long profileId){
        Boolean wasDeactivated = service.logicalDelete(profileId);
        if(wasDeactivated){
            return ResponseEntity.ok(Map.of("message", "El usuario ha sido dado de baja correctammente"));
        }else {
            return ResponseEntity.badRequest().body(Map.of("message", "Accion denegada"));
        }
    }
}
