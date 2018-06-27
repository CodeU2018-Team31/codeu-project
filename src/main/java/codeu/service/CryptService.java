package codeu.service;

import java.util.stream.Collectors;

public class CryptService {
    public String decrypt(String encryption){
        return encryption.chars().mapToObj(c -> Character.toString((char)(c - 13))).collect(Collectors.joining());
    }
}
