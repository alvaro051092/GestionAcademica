<%-- 
    Document   : Evaluaciones
    Created on : 26-jul-2017, 16:38:15
    Author     : alvar
--%>

<%@page import="Entidad.Escolaridad"%>
<%@page import="Entidad.Persona"%>
<%@page import="Logica.LoPersona"%>
<%@page import="SDT.SDT_PersonaEstudio"%>
<%@page import="Enumerado.Modo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.LoCalendario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Utilidades utilidad = Utilidades.GetInstancia();

    //----------------------------------------------------------------------------------------------------
    //CONTROL DE ACCESO
    //----------------------------------------------------------------------------------------------------
    String usuario = (String) session.getAttribute(NombreSesiones.USUARIO.getValor());
    Boolean esAdm = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ADM.getValor());
    Boolean esAlu = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ALU.getValor());
    Boolean esDoc = (Boolean) session.getAttribute(NombreSesiones.USUARIO_DOC.getValor());
    Retorno_MsgObj acceso = Seguridad.GetInstancia().ControlarAcceso(usuario, esAdm, esDoc, esAlu, utilidad.GetPaginaActual(request));

    if (acceso.SurgioError()) {
        response.sendRedirect((String) acceso.getObjeto());
    }

    //----------------------------------------------------------------------------------------------------
    ArrayList<SDT_PersonaEstudio> lstEstudio = new ArrayList<>();
    Persona persona = (Persona) LoPersona.GetInstancia().obtenerByMdlUsr(usuario).getObjeto();
    lstEstudio = LoPersona.GetInstancia().ObtenerEstudios(persona.getPerCod());

    String tblVisible = (lstEstudio.size() > 0 ? "" : "display: none;");

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Escolaridad</title>
        <jsp:include page="/masterPage/head.jsp"/>
        <script src="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Responsive/js/dataTables.responsive.min.js"></script>
        <link href="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Responsive/css/responsive.dataTables.min.css" rel="stylesheet" type="text/css"/>

        <script>
            $('.tabla_responsive').hide();
            MostrarCargando(true);

            $(document).ready(function() {



                    //---------------------------------------------------------
                    //TABLA CON REPORTES
                    //---------------------------------------------------------
                    $('.tabla_responsive').DataTable({
                        "responsive": true,
                        "processing": true,
                        deferRender: true,
                        "lengthMenu": [ [10, 20, 50, -1], [10, 20, 50, "Todos"] ],
                        pageLength: 20,
                        "fnInitComplete": function(oSettings, json) {
                                MostrarCargando(false);
                                $('.tabla_responsive').show();
                              },
                        "ordering": false,
                        dom: '',
                        "language": {
                                "url": "<%=request.getContextPath()%>/JavaScript/DataTable/lang/spanish.json"
                            }
                    });

            } );
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
                            <!-- TITULO -->
                            ESCOLARIDAD
                        </header>
                        <div class="panel-body">
                            <div class=" form">
                                <div name="cont_estudio" class="col-lg-8 col-lg-offset-2" style=' <% out.print(tblVisible); %>'>
                                    <%
                                        for (SDT_PersonaEstudio est : lstEstudio) {

                                            if (est.getInscripcion().getInsCod() == Long.valueOf("0")) {
                                                out.println("<div class='contenedor_titulo_escolaridad'><label>Sin inscripción</label></div>");
                                            } else {
                                                out.println("<div class='contenedor_titulo_escolaridad'><label>Inscripto a: " + est.getInscripcion().getNombreEstudio() + "</label></div>");
                                            }

                                            out.println("<div class='contenedor_tabla_escolaridad'>");
                                            out.println("<table class='table table-hover eliminar_margen_tabla tabla_responsive'>");
                                            out.println("<thead><tr>");
                                            out.println("<th>Materia</th>");
                                            out.println("<th>Fecha</th>");
                                            out.println("<th class='texto_derecha'>Curso</th>");
                                            out.println("<th class='texto_derecha'>Examen</th>");
                                            out.println("<th class='texto_derecha'>Estado</th>");
                                            out.println("</tr>");
                                            out.println("</thead>");
                                            out.println("<tbody>");

                                            for (Escolaridad esc : est.getEscolaridad()) {
                                                out.println("<tr>");

                                                out.println("<td>");
                                                out.println(esc.getNombreEstudio());
                                                out.println("</td>");

                                                out.println("<td>");
                                                out.println(utilidad.NuloToVacio(esc.getEscFch()));
                                                out.println("</td>");

                                                out.println("<td class='texto_derecha'>");
                                                out.println("<label>" + (esc.Revalida() ? "0" : utilidad.NuloToCero(esc.getEscCurVal())) + "</label>");
                                                out.println("</td>");

                                                out.println("<td class='texto_derecha'>");
                                                out.println("<label>" + (esc.Revalida() ? "0" : utilidad.NuloToCero(esc.getEscCalVal())) + "</label>");
                                                out.println("</td>");

                                                out.println("<td class='texto_derecha'>");
                                                out.println("<label>" + esc.getAprobacion() + "</label>");
                                                out.println("</td>");

                                                out.println("</tr>");
                                            }

                                            out.println("</tbody>");
                                            out.println("</table>");

                                            out.println("</div>");
                                        }
                                    %>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>

        <jsp:include page="/masterPage/footer.jsp"/>
        
        
    </body>
</html>