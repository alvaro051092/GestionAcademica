<%-- 
    Document   : cabezal_menu
    Created on : 01-sep-2017, 21:37:42
    Author     : alvar
--%>

<%@page import="Entidad.Persona"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Entidad.NotificacionBandeja"%>
<%@page import="Enumerado.BandejaEstado"%>
<%@page import="Enumerado.BandejaTipo"%>
<%@page import="Logica.LoBandeja"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Dominio.OpcionesDeMenu"%>
<%@page import="Entidad.Menu"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%
    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema   = utilidad.GetUrlSistema();
    
    String js_redirect = "window.location.replace('" + urlSistema + "');";
    
    String sitioActual = utilidad.GetPaginaActual(request);
    
    Boolean esAdm   = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ADM.getValor());
    Boolean esAlu   = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ALU.getValor());
    Boolean esDoc   = (Boolean) session.getAttribute(NombreSesiones.USUARIO_DOC.getValor());
    
    String usuarioNombre    = (String) session.getAttribute(NombreSesiones.USUARIO_NOMBRE.getValor());
    
    esAdm = (esAdm == null ? false : esAdm);
    esAlu = (esAlu == null ? false : esAlu);
    esDoc = (esDoc == null ? false : esDoc);
    

    Long PerCod             = (Long) session.getAttribute(NombreSesiones.USUARIO_PER.getValor());    

    List<Object> lstBandeja = new ArrayList<>();
    List<Object> lstVistos  = new ArrayList<>();
    
    Persona persona         = new Persona();
    
    if(PerCod != null)
    {
        Retorno_MsgObj retorno = (Retorno_MsgObj) LoBandeja.GetInstancia().obtenerListaByTipoEstado(PerCod, BandejaTipo.WEB, BandejaEstado.SIN_LEER);
        if(!retorno.SurgioError())
        {
            lstBandeja = retorno.getLstObjetos();
        }
        else
        {
            out.print(retorno.getMensaje().toString());
        }
        
        retorno = (Retorno_MsgObj) LoBandeja.GetInstancia().obtenerListaByTipoEstado(PerCod, BandejaTipo.WEB, BandejaEstado.LEIDA);
        if(!retorno.SurgioError())
        {
            lstVistos = retorno.getLstObjetos();
        }
        else
        {
            out.print(retorno.getMensaje().toString());
        }
        
        persona = (Persona) LoPersona.GetInstancia().obtener(PerCod).getObjeto();
    }
    
    Integer cantidad = lstBandeja.size();

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<input type="hidden" name="sga_url" id="sga_url" value="<% out.print(urlSistema); %>">
<input type="hidden" name="sga_sitioactual" id="sga_sitioactual" value="<% out.print(sitioActual); %>">
<input type="hidden" name="cbz_PerCod" id="cbz_PerCod" value="<%=PerCod%>">
<script>
    $(document).ready(function () {
       if($('#cbz_PerCod').val() == "")
       {
           <%=js_redirect%>
       }
    }); 
</script>
    
<!-- CABEZAL -->
<div class="container_fluid">
    <div class="row">
        <div class="col-xs-12 cabezal_contenedor">
            <!-- Botón Menú y Logo -->
            <div class="cabezal_contenedorLogoBoton">
                <a href="<%=urlSistema%>" ><img class='cabezal_logo' src='<%=urlSistema%>Imagenes/logo_ctc_1.png'/></a>
                						
                
                <div id="content" class="cabezal_contenedorBotonResponsive">
                    <button type="button" id="sidebarCollapse" class="cabezal_botonResponsive navbar-btn">
                        <i class="ti-menu"></i>
                    </button>
                </div>
            </div>
            <!-- -->

            <div>
                <ul class="cabezal_menu">						
                    <li>
                        <div class="dropdown">
                            <button class="cabezal_notificaciones" type="button" data-toggle="dropdown">
                                <i class="ti-bell"></i>
                                <span class="cabezal_notificacionesNumero"><%=cantidad%></span>
                            </button>
                            <ul class="dropdown-menu dropdown_arreglado cabezal_notificacionSubMenu">
                                
                                <%
                                    for(Object objeto : lstBandeja)
                                    {
                                      NotificacionBandeja bandeja = (NotificacionBandeja) objeto;
                                      
                                      out.println("<li>");
                                        out.println("<a href='#'>");
                                            out.println("<div data-toggle='modal' data-target='#PopUpMensaje' class='btn_ver_msg' data-codigo='"+ bandeja.getNotBanCod()+"'>");
                                                out.println("<div class='cabezal_notificacionAsunto'>" + bandeja.getAsuntoRecortado(30) + "</div>");
                                                out.println("<div class='cabezal_notificacionFecha'>" + bandeja.getNotBanFch() + "</div>");
                                            out.println("</div>");
                                        out.println("</a>");
                                      out.println("</li>");

                                    }
                                %>
                                
                                <!-- LEIDAS -->
                                
                                <%
                                    for(Object objeto : lstVistos)
                                    {
                                      NotificacionBandeja bandeja = (NotificacionBandeja) objeto;  
                                       
                                      out.println("<li>");
                                        out.println("<a href='#'>");
                                            out.println("<div data-toggle='modal' class='btn_ver_msg'  data-target='#PopUpMensaje' data-codigo='"+ bandeja.getNotBanCod()+"'>");
                                                out.println("<div class=''>" + bandeja.getAsuntoRecortado(30) + "</div>");
                                                out.println("<div class='cabezal_notificacionFecha'>" + bandeja.getNotBanFch() + "</div>");
                                            out.println("</div>");
                                        out.println("</a>");
                                      out.println("</li>");
                                    }
                                %>
                                
                            </ul>
                        </div>
                    </li>

                    <li>
                        <div class="dropdown hidden-xs">
                            <button class="cabezal_botonMenuUsuario dropdown-toggle" type="button" data-toggle="dropdown"><div class="cabezal_contenedorAvatar">
                                   
                                    <%
                           
                                        if(persona.getFotoBase64() == null)
                                        {
                                            out.println("<img class='cabezal_avatar' src='" + urlSistema + "Imagenes/avatar.png'/>");
                                        }
                                        else
                                        {
                                            out.println("<img class='cabezal_avatar' src='data:image/" + persona.getFotoExtension() + ";base64, " + persona.getFotoBase64() + "'/>");
                                        }

                                    %>
                                </div><div class="cabezal_nombre"><%=usuarioNombre%></div><span class="glyphicon glyphicon-triangle-bottom menu_alinearFlecha"></span></button>
                            <ul class="dropdown-menu cabezal_menuUsuario">
                                <li><a href="<%=urlSistema%>Perfil.jsp"><span class="ti-user cabezal_menuUsuarioIconos"></span> Perfil</a></li>
                                <li><a href="<%=urlSistema%>pswChange.jsp"><span class="ti-power-off cabezal_menuUsuarioIconos"></span> Cambiar contraseña</a></li>
                                <li><a href="#" class="cerrar_sesion"><span class="ti-settings cabezal_menuUsuarioIconos"></span> Cerrar Sesión</a></li>	
                            </ul>
                        </div>
                    </li>

                </ul>
            </div>
        </div>
    </div>
</div>
<!-- TERMINA CABEZAL -->




<!-- MENU -->

<nav id="sidebar" class="menu_lateral">		
    <!-- Usuario -->			
    <div class="menu_contenedorUsuario">
        <div class="menu_contenedorAvatar">
            
            <%
                

                if(persona.getFotoBase64() == null)
                {
                    out.println("<img class='menu_avatar' src='" + urlSistema + "Imagenes/avatar.png'/>");
                }
                else
                {
                    out.println("<img class='menu_avatar' src='data:image/" + persona.getFotoExtension() + ";base64, " + persona.getFotoBase64() + "'/>");
                }

            %>
            
            <div class="overlay">
                <a class="texto_cambiarFoto" href="<%=urlSistema%>uploadFoto.jsp">CAMBIAR</a>
            </div>
        </div>
        <div class="menu_usuarioEnMenu">
            <span class="menu_usuarioNombre"><a href="<%=urlSistema%>Perfil.jsp"><%=usuarioNombre%></a></span>
            <ul>
                <li class="menu_usuarioListado"><a href="<%=urlSistema%>pswChange.jsp">Cambiar contraseña</a></li>
                <li class="menu_usuarioListado"><a href="#" class="cerrar_sesion">Cerrar Sesión</a></li>
            </ul>
        </div>
    </div>

        
     <%
        if(esAdm)
        {               

            out.println("<ul class='list-unstyled components menu_contenedor'>");

            for(Menu menu : OpcionesDeMenu.GetInstancia().getLstAdministrador())
            {
                if(menu.getMenIsParent())
                {
                    out.println("<li><a href='#sub_" + menu.getMenCod() + "' data-toggle='collapse' aria-expanded='false'>" + menu.getMenNom() + "<span class='ti-angle-right'></span></a>");
                        
                        out.println("<ul class='collapse list-unstyled menu_submenu' id='sub_" + menu.getMenCod() + "'>");
                            for(Menu subMenu : menu.getLstSubMenu())
                            {
                                out.println("<li><a href='" + subMenu.getMenUrl() + "'>" + subMenu.getMenNom() + "</a></li>");
                            }
                        out.println("</ul>");
                        
                    out.println("</li>");
                }
                else
                {
                    out.println("<li><a href='" + menu.getMenUrl() + "'>" + menu.getMenNom() + "</a></li>");                    
                }
            }
            
            out.println("</ul>");
        }

        if(esDoc)
        {
            out.println("<label>Docente</label>");
            out.println("<ul class='list-unstyled components menu_contenedor'>");
            
            for(Menu menu : OpcionesDeMenu.GetInstancia().getLstDocente())
            {
                out.println("<li><a href='" + menu.getMenUrl() + "'>" + menu.getMenNom() + "</a></li>");
            }
            
            out.println("</ul>");
        }
        
        if(esAlu)
        {
            out.println("<label>Alumno</label>");
            out.println("<ul class='list-unstyled components menu_contenedor'>");
            
            for(Menu menu : OpcionesDeMenu.GetInstancia().getLstAlumno())
            {
                out.println("<li><a href='" + menu.getMenUrl() + "'>" + menu.getMenNom() + "</a></li>");
            }
            
            out.println("</ul>");

        }


    %>   
        
    <!-- Sidebar Links -->
    
</nav>
        
      
<script>
    $(document).ready(function() {

        var urlAct = $('#sga_url').val();

        MostrarCargando(false);

            $('.cerrar_sesion').click(function(event) {

                    MostrarCargando(true);

                    // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                    $.post(urlAct + 'Login', {
                            pAction : "FINALIZAR"
                    }, function(responseText) {
                            var obj = JSON.parse(responseText);

                            if(obj.tipoMensaje == 'ERROR')
                            {
                                MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                                MostrarCargando(false);
                            }
                            else
                            {
                                <%=js_redirect%>
                            }
                    });

            });

    });
</script>