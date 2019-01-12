<%-- 
    Document   : DefPersonaWW
    Created on : 30-jun-2017, 20:44:29
    Author     : alvar
--%>

<%@page import="Enumerado.IconClass"%>
<%@page import="Logica.LoParametro"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Entidad.Persona"%>
<%@page import="java.util.List"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.LoPersona"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%

    LoPersona loPersona = LoPersona.GetInstancia();
    Utilidades utilidad = Utilidades.GetInstancia();

    String urlSistema = utilidad.GetUrlSistema();
    
    //----------------------------------------------------------------------------------------------------
    //CONTROL DE ACCESO
    //----------------------------------------------------------------------------------------------------
    String usuario = (String) session.getAttribute(Enumerado.NombreSesiones.USUARIO.getValor());
    Boolean esAdm = (Boolean) session.getAttribute(Enumerado.NombreSesiones.USUARIO_ADM.getValor());
    Boolean esAlu = (Boolean) session.getAttribute(Enumerado.NombreSesiones.USUARIO_ALU.getValor());
    Boolean esDoc = (Boolean) session.getAttribute(Enumerado.NombreSesiones.USUARIO_DOC.getValor());
    Retorno_MsgObj acceso = Logica.Seguridad.GetInstancia().ControlarAcceso(usuario, esAdm, esDoc, esAlu, utilidad.GetPaginaActual(request));

    if (acceso.SurgioError()) {
        response.sendRedirect((String) acceso.getObjeto());
    }

    //----------------------------------------------------------------------------------------------------
    Retorno_MsgObj retorno = loPersona.obtenerLista();
    List<Object> lstObjeto = new ArrayList<>();

    if (!retorno.SurgioError()) {
        lstObjeto = retorno.getLstObjetos();
    } else {
        out.print(retorno.getMensaje().getMensaje());
    }

    String tblPersonaVisible = (lstObjeto.size() > 0 ? "" : "display: none;");

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Personas</title>
        <jsp:include page="/masterPage/head.jsp"/>
        <jsp:include page="/masterPage/head_tables.jsp"/>
        
        <script>
            $(document).ready(function () {
                    
                    
                    $('#btn_sincronizar').on('click', function (e) {
                        
                        $.post('<% out.print(urlSistema); %>ABM_Persona', {
                            pAction: "SINCRONIZAR_MOODLE"
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);

                            MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                            if (obj.tipoMensaje != 'ERROR')
                            {
                                location.reload();
                            }

                        });

        
                    });
              });
        </script>
            
    </head>
    <body>
        <jsp:include page="/masterPage/NotificacionError.jsp"/>
        <jsp:include page="/masterPage/cabezal_menu.jsp"/>

        <!-- CONTENIDO -->
        <div class="contenido" id="contenedor">

            <div class="row">
                <div class="col-lg-12">
                    <section class="panel">
                        <header class="panel-heading">
                            PERSONAS
                            <span class="tools pull-right">
                                <a href="<% out.print(urlSistema); %>Definiciones/DefPersona.jsp?MODO=<% out.print(Enumerado.Modo.INSERT); %>" title="Ingresar" class="<%=IconClass.ADD.getValor()%>"> </a>
                                <%  
                                    if(LoParametro.GetInstancia().obtener().getParUtlMdl())
                                     {
                                 %>
                                     <a href="#" title="Sincronizar" class="<%=IconClass.SYNC.getValor()%>" name="btn_sincronizar" id="btn_sincronizar"> </a>
                                 <%
                                     }
                                 %>
                            </span>
                            
                        </header>
                        <div class="panel-body">
                            <div class=" form">
                                <table id='tbl_ww' class='table table-hover' style=' <% out.print(tblPersonaVisible); %>'>
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th></th>
                                            <th>Código</th>
                                            <th>Nombre</th>
                                            <th>Apellido</th>
                                            <th>Documento</th>
                                            <th>Email</th>
                                            <th>Filial</th>
                                            <th>Número en libra</th>
                                            <th>Docente</th>
                                            <th>Alumno</th>
                                            <th>Administrador</th>
                                        </tr>
                                    </thead>
                                    <% for (Object objeto : lstObjeto) {
                                            Persona persona = (Persona) objeto;

                                    %>
                                    <tr>
                                        <td><a href="<% out.print(urlSistema); %>Definiciones/DefPersona.jsp?MODO=<% out.print(Enumerado.Modo.DELETE); %>&pPerCod=<% out.print(persona.getPerCod()); %>" name="btn_eliminar" id="btn_eliminar" class="<%=IconClass.DELETE.getValor()%>"></a></td>
                                        <td><a href="<% out.print(urlSistema); %>Definiciones/DefPersona.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pPerCod=<% out.print(persona.getPerCod()); %>" name="btn_editar" id="btn_editar" class="<%=IconClass.EDIT.getValor()%>"></a></td>
                                        <td><% out.print(utilidad.NuloToCero(persona.getPerCod())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(persona.getPerNom())); %></td>
                                        <td><% out.print(utilidad.NuloToVacio(persona.getPerApe())); %></td>
                                        <td><% out.print(utilidad.NuloToVacio(persona.getPerDoc())); %></td>
                                        <td><% out.print(utilidad.NuloToVacio(persona.getPerEml())); %></td>
                                        <td><% out.print(utilidad.NuloToVacio(persona.getPerFil().getFilialNom())); %></td>
                                        <td><% out.print(utilidad.NuloToCero(persona.getPerNroLib())); %></td>
                                        <td><% out.print(utilidad.BooleanToSiNo(persona.getPerEsDoc())); %></td>
                                        <td><% out.print(utilidad.BooleanToSiNo(persona.getPerEsAlu())); %></td>
                                        <td><% out.print(utilidad.BooleanToSiNo(persona.getPerEsAdm())); %></td>
                                    </tr>
                                    <%
                                        }
                                    %>
                                </table>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>

        <jsp:include page="/masterPage/footer.jsp"/>

    </body>
</html>