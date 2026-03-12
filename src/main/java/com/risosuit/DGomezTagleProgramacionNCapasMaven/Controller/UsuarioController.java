package com.risosuit.DGomezTagleProgramacionNCapasMaven.Controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.ML.Direccion;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.ML.ErroresArchivo;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.ML.Pais;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.ML.Result;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.ML.Usuario;

import jakarta.servlet.http.HttpSession;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.ML.Rol;

import org.apache.catalina.connector.Response;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("Usuario")
public class UsuarioController {
    private static String rutaBase = "http://127.0.0.1:8081/";

    // METODOS CONTROLADOR USUARIO
    @GetMapping("")
    public String GetAll(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        Result Result = null;

        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(rutaBase + "api/usuario", HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Usuario>>() {
                });
        if (responseEntity.getStatusCode().value() == 200) {
            Result = responseEntity.getBody();
        }
        model.addAttribute("usuarios", Result.Objects);
        model.addAttribute("Usuario", new Usuario());

        ResponseEntity<Result<Rol>> responseEntityRol = restTemplate.exchange(rutaBase + "api/rol", HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Rol>>() {
                });
        if (responseEntityRol.getStatusCode().value() == 200) {
            Result = responseEntityRol.getBody();
        }
        model.addAttribute("Roles", Result.Objects);
        return "GetAll";
    }

    @GetMapping("{idUsuario}")
    public String GetByIdDetalle(@PathVariable("idUsuario") int idUsuario, Model model) {
        // Result Result = usuarioJPADAOImplementation.GetById(idUsuario);
        Result Result = null;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(rutaBase + "api/usuario/" + idUsuario,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Usuario>>() {
                });
        if (responseEntity.getStatusCode().value() == 200) {
            Result = responseEntity.getBody();
        }
        model.addAttribute("usuario", Result.Object);
        ResponseEntity<Result<Rol>> responseEntityRol = restTemplate.exchange(rutaBase + "api/rol", HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Rol>>() {
                });
        if (responseEntityRol.getStatusCode().value() == 200) {
            Result = responseEntityRol.getBody();
        }
        model.addAttribute("Roles", Result.Objects);

        ResponseEntity<Result<Pais>> responseEntityPais = restTemplate.exchange(rutaBase + "api/pais", HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Pais>>() {
                });
        if (responseEntityPais.getStatusCode().value() == 200) {
            Result = responseEntityPais.getBody();
        }
        model.addAttribute("Paises", Result.Objects);
        model.addAttribute("Direccion", new Direccion());
        return "DetalleUsuario";

    }

    @PostMapping("")
    public String Busqueda(@ModelAttribute("Usuario") Usuario usuario, Model model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);
            RestTemplate restTemplate = new RestTemplate();
            Result Result = null;
            ResponseEntity<Result<Usuario>> responseEntityUsuario = restTemplate.exchange(
                    rutaBase + "api/usuario/busqueda",
                    HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Result<Usuario>>() {
                    });
            if (responseEntityUsuario.getStatusCode().value() == 200) {
                Result = responseEntityUsuario.getBody();
            }

            model.addAttribute("usuarios", Result.Objects);
            model.addAttribute("Usuario", usuario);
            ResponseEntity<Result<Rol>> responseEntityRol = restTemplate.exchange(rutaBase + "api/rol", HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<Rol>>() {
                    });
            if (responseEntityRol.getStatusCode().value() == 200) {
                Result = responseEntityRol.getBody();
            }
            model.addAttribute("Roles", Result.Objects);

        } catch (Exception e) {

        }

        return "GetAll";

    }

    // Carga la vista de carga masiva
    @GetMapping("cargamasiva")
    public String CargaMasiva() {
        return "CargaMasiva";
    }

    @PostMapping("cargamasiva")
    public String CargaMasiva(@RequestParam("archivo") MultipartFile archivo,
            RedirectAttributes redirectAttributes) {

        try {
            if (archivo != null) {
                String nombreOriginal = archivo.getOriginalFilename();
                String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf(".") + 1).toLowerCase();

                if (extension.equals("txt") || extension.equals("xlsx")) {
                    Result Result = null;
                    try {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                        body.add("archivo", archivo.getResource());

                        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
                        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
                        ResponseEntity<Result<String>> response = restTemplate.exchange(
                                rutaBase + "api/usuario/validarcarga",
                                HttpMethod.POST,
                                requestEntity,
                                new ParameterizedTypeReference<Result<String>>() {
                                });
                        if (response.getStatusCode().value() == 200) {
                            Result = response.getBody();
                            if (Result.Correct) {
                                redirectAttributes.addFlashAttribute("idRuta", Result.Object);
                                redirectAttributes.addFlashAttribute("Success", "Archivo Validado Correctamente");

                            } else {
                                redirectAttributes.addFlashAttribute("Error", "El formato del archivo es inválido");
                            }
                        }

                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode().value() == 400) {
                            Result = e.getResponseBodyAs(new ParameterizedTypeReference<Result<String>>() {
                            });
                            List<String> errores = (List<String>) Result.Objects;
                            System.out.println(errores);

                            redirectAttributes.addFlashAttribute("errores", errores);
                            return "redirect:cargamasiva";

                        } else {
                            redirectAttributes.addFlashAttribute("Error",
                                    "Error al procesar el archivo: " + e.getMessage());
                        }

                    }
                    ResponseEntity<Result> responseEntity = null;
                    redirectAttributes.addFlashAttribute("Success", "El formato del archivo es válido");
                } else {
                    redirectAttributes.addFlashAttribute("Error",
                            "Formato de archivo no válido. Solo se permiten archivos .txt o .xlsx");
                }
            } else {
                redirectAttributes.addFlashAttribute("Error", "No se ha seleccionado ningún archivo");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("Error", "Error al validar el archivo: " + e.getMessage());
        }

        return "redirect:cargamasiva";
    }

    @GetMapping("CargaMasivaProcesar")
    public String procesarArchivo(@RequestParam("idRuta") String idRuta,
            RedirectAttributes redirectAttributes) {

        if (idRuta != null) {

            try {
                RestTemplate restTemplate = new RestTemplate();
                Result Result = null;
                ResponseEntity<Result> response = restTemplate.exchange(
                        rutaBase + "api/usuario/procesarcarga?key=" + idRuta,
                        HttpMethod.POST,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result>() {
                        });
                if (response.getStatusCode().value() == 200) {
                    Result = response.getBody();
                    if (Result.Correct) {
                        redirectAttributes.addFlashAttribute("Success", "Archivo Procesado Correctamente");
                    } else {
                        redirectAttributes.addFlashAttribute("Error", Result.MessageException);
                    }
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("Error", "Error al procesar el archivo: " + e.getMessage());
            }

        } else {
            redirectAttributes.addFlashAttribute("Error", "No se ha proporcionado una ruta de archivo válida");

        }

        return "redirect:/Usuario";
    }

    @PostMapping("guardarImagen")
    public String guardarImagen(
            @ModelAttribute("Usuario") Usuario usuario,
            @RequestParam("imagen") MultipartFile imagen,
            Model model,
            RedirectAttributes redirectAttributes) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("idUsuario", usuario.getIdUsuario());
        body.add("imagen", imagen.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Result> response = restTemplate.exchange(
                rutaBase + "api/usuario/imagen",
                HttpMethod.PATCH,
                requestEntity,
                new ParameterizedTypeReference<Result>() {
                });
        if (response.getStatusCode().value() == 200) {
            result = response.getBody();
            if (result.Correct) {
                redirectAttributes.addFlashAttribute("Success", "Imagen Actualizada Correctamente");
            } else {
                redirectAttributes.addFlashAttribute("Error", "Algo Salió mal :( ");
            }
            return "redirect:/Usuario/" + usuario.getIdUsuario();
        }

        return "redirect:/Usuario/" + usuario.getIdUsuario();
    }

    @GetMapping("Delete/{idUsuario}")
    public String DeleteUsuario(@PathVariable("idUsuario") int idUsuario, Model model,
            RedirectAttributes redirectAttributes) {
        Result Result = null;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(rutaBase + "api/usuario/" + idUsuario,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Usuario>>() {
                });
        if (responseEntity.getStatusCode().value() == 200) {
            Result = responseEntity.getBody();
        }
        if (Result.Correct) {
            redirectAttributes.addFlashAttribute("Success", "El usuario fue eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("Error", "El usuario No fue eliminado correctamente");
        }
        return "redirect:/Usuario";
    }

    @GetMapping("Form")
    public String Formulario(Model model) {
        Result Result = null;
        RestTemplate restTemplate = new RestTemplate();
        model.addAttribute("Usuario", new Usuario());
        ResponseEntity<Result<Pais>> responseEntityPais = restTemplate.exchange(rutaBase + "api/pais", HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Pais>>() {
                });
        if (responseEntityPais.getStatusCode().value() == 200) {
            Result = responseEntityPais.getBody();
        }

        model.addAttribute("Paises", Result.Objects);
        ResponseEntity<Result<Rol>> responseEntityRol = restTemplate.exchange(rutaBase + "api/rol", HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Rol>>() {
                });
        if (responseEntityRol.getStatusCode().value() == 200) {
            Result = responseEntityRol.getBody();
        }
        model.addAttribute("Roles", Result.Objects);

        return "Formulario";
    }

    @PostMapping("Form")
    public String AddUsuario(
            @ModelAttribute("Usuario") Usuario usuario,
            @RequestParam("imagen") MultipartFile imagen,
            Model model,
            RedirectAttributes redirectAttributes) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("usuario", usuario);
        body.add("imagen", imagen.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                    rutaBase + "api/usuario",
                    HttpMethod.POST,
                    requestEntity,
                    Result.class);
            if (response.getStatusCode().value() == 200) {
                result = response.getBody();
                if (result != null && result.Correct) {
                    redirectAttributes.addFlashAttribute("Success", "Usuario fue guardado correctamente");
                    return "redirect:/Usuario";
                } else {
                    redirectAttributes.addFlashAttribute("Error", "Algo salió Mal :(");
                }
                return "redirect:/Form";
            }

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 400) {
                redirectAttributes.addFlashAttribute("Error",
                        "Usuario no fue agregado correctamente, verifique los datos");
                result = e.getResponseBodyAs(new ParameterizedTypeReference<Result<Map<String, String>>>() {
                });
                model.addAttribute("Usuario", usuario);
                Map<String, String> respuesta = (Map<String, String>) result.Object;

                for (Map.Entry<String, String> entry : respuesta.entrySet()) {
                    System.out.println("Clave: " + entry.getKey() + ", Valor: " + entry.getValue());
                    model.addAttribute(entry.getKey(), entry.getValue());
                }

                Result Result = null;

                ResponseEntity<Result<Pais>> responsePais = restTemplate.exchange(
                        rutaBase + "api/pais",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<Pais>>() {
                        });

                if (responsePais.getStatusCode().value() == 200) {
                    Result = responsePais.getBody();
                }

                model.addAttribute("Paises", Result.Objects);

                ResponseEntity<Result<Rol>> responseRol = restTemplate.exchange(
                        rutaBase + "api/rol",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<Rol>>() {
                        });

                if (responseRol.getStatusCode().value() == 200) {
                    Result = responseRol.getBody();
                }
                model.addAttribute("Roles", Result.Objects);
                model.addAttribute("Failed", "Usuario no fue agregado correctamente, verifique los datos");
                return "Formulario";
            }
        }

        return "redirect:/Usuario";
    }

    @PostMapping("/UpdateUser/{idUsuario}")
    public String UpdateUsuario(
            @PathVariable("idUsuario") int idUsuario,
            @ModelAttribute("Usuario") Usuario usuario,
            Model model,
            RedirectAttributes redirectAttributes) {

        Result Result = null;
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);
            ResponseEntity<Result<Usuario>> responseEntityUsuario = restTemplate.exchange(
                    rutaBase + "api/usuario",
                    HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<Result<Usuario>>() {
                    });

            if (responseEntityUsuario.getStatusCode().value() == 200) {
                Result = responseEntityUsuario.getBody();
                if (Result.Correct) {
                    redirectAttributes.addFlashAttribute("SuccessEdicion", "Usuario Editado correctamente");
                } else {
                    redirectAttributes.addFlashAttribute("FailedEdicion", "Algo Salió Mal");
                }

                return "redirect:/Usuario/" + idUsuario;
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                redirectAttributes.addFlashAttribute("FailedEdicion", "Usuario No Encontrado");
                ResponseEntity<Result<Rol>> responseEntityRol = restTemplate.exchange(rutaBase + "api/rol",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<Rol>>() {
                        });
                if (responseEntityRol.getStatusCode().value() == 200) {
                    Result<Rol> resultRol = responseEntityRol.getBody();
                    model.addAttribute("Roles", resultRol.Objects);
                }

                ResponseEntity<Result<Pais>> responseEntityPais = restTemplate.exchange(rutaBase + "api/pais",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<Pais>>() {
                        });
                if (responseEntityPais.getStatusCode().value() == 200) {
                    Result<Pais> resultPais = responseEntityPais.getBody();
                    model.addAttribute("Paises", resultPais.Objects);
                }

                redirectAttributes.addFlashAttribute("FailedEdicion", "Usuario No Fue Editado correctamente");
                return "redirect:/Usuario/" + idUsuario;

            }
            if (e.getStatusCode().value() == 400) {
                Result<Map<String, String>> result = e
                        .getResponseBodyAs(new ParameterizedTypeReference<Result<Map<String, String>>>() {
                        });
                Map<String, String> errores = result.Object;
                for (Map.Entry<String, String> entry : errores.entrySet()) {
                    System.out.println("Clave: " + entry.getKey() + ", Valor: " + entry.getValue());
                    model.addAttribute(entry.getKey(), entry.getValue());
                }
                redirectAttributes.addFlashAttribute("FailedEdicion",
                        "Usuario No Fue Editado correctamente, Verifique los datos");
                ResponseEntity<Result<Rol>> responseEntityRol = restTemplate.exchange(rutaBase + "api/rol",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<Rol>>() {
                        });
                if (responseEntityRol.getStatusCode().value() == 200) {
                    Result<Rol> resultRol = responseEntityRol.getBody();
                    model.addAttribute("Roles", resultRol.Objects);
                }

                ResponseEntity<Result<Pais>> responseEntityPais = restTemplate.exchange(rutaBase + "api/pais",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<Pais>>() {
                        });
                if (responseEntityPais.getStatusCode().value() == 200) {
                    Result<Pais> resultPais = responseEntityPais.getBody();
                    model.addAttribute("Paises", resultPais.Objects);

                }
            }
        }

        return "redirect:/Usuario/" + idUsuario;

    }

    @GetMapping("DeleteDireccion/{idDireccion}/{idUsuario}")

    // METODOS CONTROLADOR DIRECCION
    public String DeleteDireccion(@PathVariable("idDireccion") int idDireccion,
            @PathVariable("idUsuario") int idUsuario, RedirectAttributes redirectAttributes) {
        Result Result = null;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Direccion>> responseEntity = restTemplate.exchange(
                rutaBase + "api/direccion/" + idDireccion,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Direccion>>() {
                });
        if (responseEntity.getStatusCode().value() == 200) {
            Result = responseEntity.getBody();
        }
        if (Result.Correct) {
            redirectAttributes.addFlashAttribute("SuccessDeleteDireccion", "La Direccion fue eliminada correctamente");
        } else {
            redirectAttributes.addFlashAttribute("ErrorDeleteDireccion", "La Direccion No fue eliminada correctamente");
        }
        return "redirect:/Usuario/" + idUsuario;
    }

    @PostMapping("/agregarDireccion/{idUsuario}")
    public String AddDireccion(@PathVariable("idUsuario") int idUsuario,
            @ModelAttribute("direccion") Direccion direccion,

            Model model, RedirectAttributes redirectAttributes) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {

            HttpEntity<Direccion> requestEntity = new HttpEntity<>(direccion, headers);
            ResponseEntity<Result<Direccion>> responseDireccion = restTemplate.exchange(
                    rutaBase + "api/direccion/" + idUsuario,
                    HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Result<Direccion>>() {
                    });
            if (responseDireccion.getStatusCode().value() == 200) {
                Result Result = responseDireccion.getBody();
                if (Result.Correct) {

                    redirectAttributes.addFlashAttribute("SuccessAddDireccion", "Direccion Agregada correctamente");
                } else {
                    redirectAttributes.addFlashAttribute("ErrorAddDireccion", "Algo Salió Mal");
                }
            }

        } catch (HttpClientErrorException e) {
            System.out.println(e.getStatusCode().value());
            if (e.getStatusCode().value() == 400) {
                Result<Direccion> Result = e.getResponseBodyAs(new ParameterizedTypeReference<Result<Direccion>>() {
                });
                direccion = Result.Object;

                if (!Result.Correct) {

                    redirectAttributes.addFlashAttribute("direccion", direccion);
                    redirectAttributes.addFlashAttribute("ErrorAddDireccion",
                            "Direccion No Fue Agregado correctamente, Verifique los datos");
                    ResponseEntity<Result<Pais>> responseEntityPais = restTemplate.exchange(rutaBase + "api/pais",
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            new ParameterizedTypeReference<Result<Pais>>() {
                            });
                    if (responseEntityPais.getStatusCode().value() == 200) {
                        Result ResultPais = responseEntityPais.getBody();
                        model.addAttribute("paises", ResultPais.Objects);
                    }

                }

            }

        }

        return "redirect:/Usuario/" + idUsuario;
    }

    @PostMapping("/UpdateDireccion/{idUsuario}")
    public String UpdateDireccion(@PathVariable("idUsuario") int idUsuario,
            @ModelAttribute("direccion") Direccion direccion,
            BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {

            HttpEntity<Direccion> requestEntity = new HttpEntity<>(direccion, headers);
            ResponseEntity<Result<Direccion>> responseDireccion = restTemplate.exchange(
                    rutaBase + "api/direccion/" + idUsuario,
                    HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<Result<Direccion>>() {
                    });
            if (responseDireccion.getStatusCode().value() == 200) {
                Result Result = responseDireccion.getBody();
                if (Result.Correct) {
                    redirectAttributes.addFlashAttribute("SuccessEdicionDireccion", "Direccion Editado correctamente");
                } else {
                    redirectAttributes.addFlashAttribute("ErrorEdicionDireccion", "Algo Salió Mal :(");
                }
            }

        } catch (HttpClientErrorException e) {
            System.out.println(e.getStatusCode().value());
            if (e.getStatusCode().value() == 400) {
                Result<Direccion> Result = e.getResponseBodyAs(new ParameterizedTypeReference<Result<Direccion>>() {
                });
                direccion = Result.Object;

                if (!Result.Correct) {

                    redirectAttributes.addFlashAttribute("direccion", direccion);

                    redirectAttributes.addFlashAttribute("ErrorAddDireccion",
                            "Direccion No Fue Agregado correctamente, Verifique los datos");
                    ResponseEntity<Result<Pais>> responseEntityPais = restTemplate.exchange(rutaBase + "api/pais",
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            new ParameterizedTypeReference<Result<Pais>>() {
                            });
                    if (responseEntityPais.getStatusCode().value() == 200) {
                        Result ResultPais = responseEntityPais.getBody();
                        model.addAttribute("paises", ResultPais.Objects);
                    }
                    redirectAttributes.addFlashAttribute("ErrorEdicionDireccion", "La dirección no pudo ser editada");
                    redirectAttributes.addFlashAttribute("IdDireccion", direccion.getIdDireccion());
                    return "redirect:/Usuario/" + idUsuario;
                }

            }

        }

        return "redirect:/Usuario/" + idUsuario;

    }

}
