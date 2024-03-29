package com.example.stock.controllers;

import com.example.stock.infra.security.TokenService;
import com.example.stock.repositories.UserRepository;
import com.example.stock.user.AuthenticationDTO;
import com.example.stock.user.LoginResponseDTO;
import com.example.stock.user.RegisterDTO;
import com.example.stock.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var user = (User) auth.getPrincipal();
        var token = tokenService.generateToken((User) auth.getPrincipal());
        var role = getRoleByName(user.getLogin());

        return ResponseEntity.ok(new LoginResponseDTO(token, role));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }

    public String getRoleByName(String name) {
        User user = repository.findUserRoleByLogin(name);
        if (user != null) {
            return user.getRole().getRole();
        } else {
            // Se o usuário não for encontrado, você pode lançar uma exceção, retornar um valor padrão ou lidar de outra maneira.
            throw new IllegalArgumentException("Usuário não encontrado com o nome de login: " + name);
        }
    }

}
