<%-- 
    Document   : login
    Created on : 01-sep-2017, 20:21:58
    Author     : alvar
--%>

<%@page import="Entidad.Persona"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Utiles.Mensajes"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Entidad.Parametro"%>
<%@page import="Logica.LoParametro"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Logica.LoIniciar"%>
<%
    LoIniciar iniciar_sistema = new LoIniciar();
    iniciar_sistema.Iniciar(request);

    Utilidades util = Utilidades.GetInstancia();

    String urlSistema = util.GetInstancia().GetUrlSistema();
    
    String js_redirect = "window.location.replace('" + urlSistema + "');";

    Parametro param = LoParametro.GetInstancia().obtener();
    
    
    
    /* LOGIN */
    Mensajes mensaje    = new Mensajes("Error al iniciar sesión", TipoMensaje.ERROR);
    Boolean mostrarMensaje  = false;
    String usr              = request.getParameter("pUser");
    if(usr!=null)
    {
        mostrarMensaje  = true;
        String psw      = request.getParameter("pPass"); 

       
        LoPersona loPersona = LoPersona.GetInstancia();
        Seguridad seguridad = Seguridad.GetInstancia();


        if(loPersona.IniciarSesion(usr, seguridad.cryptWithMD5(psw)))
        {
            //Inicio correctamente
            
            session.setAttribute(NombreSesiones.USUARIO.getValor(), usr);
            
            Persona persona = (Persona) LoPersona.GetInstancia().obtenerByMdlUsr(usr).getObjeto();
            
            
            session.setAttribute(NombreSesiones.USUARIO_NOMBRE.getValor(), persona.getPerNom());
            session.setAttribute(NombreSesiones.USUARIO_ADM.getValor(), persona.getPerEsAdm());
            session.setAttribute(NombreSesiones.USUARIO_ALU.getValor(), persona.getPerEsAlu());
            session.setAttribute(NombreSesiones.USUARIO_DOC.getValor(), persona.getPerEsDoc());
            session.setAttribute(NombreSesiones.USUARIO_PER.getValor(), persona.getPerCod());
            
            
            String web = "login.jsp";
            if(persona.getPerEsAdm())
            {
                web = "Definiciones/DefCalendarioGrid.jsp";
            }else if(persona.getPerEsDoc())
            {
                web = "Docente/EstudiosDictados.jsp";
            }else if(persona.getPerEsAlu())
            { 
                web = "Alumno/Evaluaciones.jsp";
            }
            else
            {
                web = "Error.jsp?pMensaje=No tiene definido un tipo de usuario, contacte con el administrador";
            }
            
            request.getRequestDispatcher(web).forward(request, response);
            
        }
        else
        {

            //No inicio correctamente
            mensaje = new Mensajes("Usuario o contraseña incorrectos", TipoMensaje.ERROR);
        }
    }

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - CTC</title>
        
        <meta http-equiv="X-UA-Compatible" content="IE=9; IE=10; IE=11; IE=EDGE" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>	
        <meta name="theme-color" content="#628ec9">

        <link rel="manifest" href="<%=request.getContextPath()%>/manifest.json">
        
        <script src="<%=request.getContextPath()%>/JavaScript/jquery-3.2.1.min.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/Bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        
        <link media="screen" href="<%=request.getContextPath()%>/Estilos/sga_estyle.min.css"  rel="stylesheet" type="text/css"/>
        <link media="screen" href="<%=request.getContextPath()%>/Bootstrap/css/bootstrap.min.css"  rel="stylesheet">
        
        <link media="screen" href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i" rel="stylesheet">
        <link media="screen" rel="stylesheet" href="<%=request.getContextPath()%>/JavaScript/FontAwesome/css/font-awesome.min.css">
        <link rel="shortcut icon" type="image/png" href="<%=request.getContextPath()%>/Imagenes/ctc_ic.png"/>

        <script>
    
                    function MostrarMensaje(tipoMensaje, mensaje)
                    {

                        MostrarCargando(false);

                        $('#txtError').text(mensaje);

                        if(tipoMensaje == 'ERROR')
                        {
                            $("#msgError").attr('class', 'alert alert-danger div_msg');
                        }

                        if(tipoMensaje == 'ADVERTENCIA')
                        {
                            $("#msgError").attr('class', 'alert alert-warning div_msg');
                        }

                        if(tipoMensaje == 'MENSAJE')
                        {
                            $("#msgError").attr('class', 'alert alert-success div_msg');
                        }

                        $("#msgError").show();

                        setTimeout(function(){
                            //do what you need here
                            $("#msgError").hide();
                        }, 2000);
                    }


                    function MostrarCargando(mostrar)
                    {
                        if(mostrar)
                        {
                            $("#div_cargando").attr('class', 'div_cargando_load');
                        }
                        else
                        {
                            $("#div_cargando").attr('class', 'div_cargando');
                        }
                    }

                    $(document).ready(function () {
                        MostrarCargando(false);

                        $('#sidebarCollapse').on('click', function () {
                            $('#sidebar').toggleClass('active');
                        });
                    });

    </script>
        
    </head>
    <body class="body_clase">
        <jsp:include page="/masterPage/NotificacionError.jsp"/>
        
        <div class="login_fondo">		
            <div class="login_contenedor">
                <%                        if (param.getParUtlMdl()) {
                        out.println("<div class='login_aulas'>"
                                + "<a href='" + param.getParUrlMdl() + "'>"
                                + "AULAS <span class='ti-arrow-right'>"
                                + "</span></a></div>");
                    }
                %>



                <div class="login_contenedorImg"><img src="Imagenes/ctc.png" /></div>
                <h1 class="login_titulo">LOGIN</h1>
                <p class="login_texto">Bienvenido a Gestión, el servicio a estudiantes del Instituto CTC - Colonia.</p>
                <form action="#" method="post">
                    <div class="login_form">
                        <input type="text" class="form-control login_inputBorde login_inputNumero" id="pUser" name="pUser" placeholder="Usuario">
                        <input type="password" class="form-control login_inputPass" id="pPass" name="pPass" placeholder="Contraseña">
                    </div>

                    <button type="submit" name="btnLogin" id="btnLogin" class="login_boton">INGRESAR</button>
                </form>


                <a href="pswSolRecovery.jsp" class="login_olvideContrasena">¿Olvidaste tu contraseña?</a>		

            </div>
        </div>

        <div>
            <div id="div_pop_bkgr" name="div_pop_bkgr"></div>

            <div id="div_cargando" name="div_cargando">
                <div class="loading"></div>
            </div>

        </div>

       

        <script>
            $(document).ready(function () {
                MostrarCargando(false);

               <%
                   if(mostrarMensaje)
                   {
                       out.println("MostrarMensaje('"+mensaje.getTipoMensaje()+"','"+mensaje.getMensaje()+"');");
                   }
               %>
         
            });
        </script>
    </body>
</html>
