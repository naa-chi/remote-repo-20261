package com.gTransitProject.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Entity
@Table(name = "auths")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Schema(description = "Identificador de la autorizacion")
private Integer authId;

@Schema(description = "Descripcion de la solicitud")
@NotBlank(message = "La descripcion es obligatoria")
private String requestDescription;

@Schema(description = "Ciudad de origen")
@NotBlank(message = "La ciudad origen es obligatoria")
private String originCity;

@Schema(description = "Ciudad de destino")
@NotBlank(message = "La ciudad destino es obligatoria")
private String destinationCity;

@Schema(description = "Codigo unico de autorizacion")
@NotBlank(message = "El codigo de autorizacion es obligatorio")
@Column(unique = true)
private String authCode;

@Schema(description = "Codigo del supervisor")
@NotBlank(message = "El codigo del supervisor es obligatorio")
private String supervisorCode;

@Schema(description = "Estado de la autorizacion")
private Boolean authorized;

}
