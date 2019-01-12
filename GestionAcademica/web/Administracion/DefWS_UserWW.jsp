<%-- 
    Document   : DefWS_UserWW
    Created on : 16-ago-2017, 8:32:26
    Author     : alvar
--%>
<%@page import="Enumerado.Modo"%>
<%@page import="Entidad.WS_Bit"%>
<%@page import="Logica.LoWS"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%

    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();

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
    
    if(request.getParameter("boton_confirmar") != null) {
        if(request.getParameter("boton_confirmar").equals("DEPURAR"))
        {
            LoWS.GetInstancia().EliminarBitacoraBeforeDate();
        }
    }
    
    List<Object> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) LoWS.GetInstancia().BitacoraObtenerLista();
    if(!retorno.SurgioError()) {
        if(!retorno.SurgioErrorListaRequerida()) 
        {
            lstObjeto = retorno.getLstObjetos();
        }
    } else {
        out.print(retorno.getMensaje().toString());
    }

    String tblVisible = (lstObjeto.size() > 0 ? "" : "display: none;");

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Bitácora</title>
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
                            BITÁCORA DE SERVICIOS
                            <!-- BOTONES -->
                            <span class="tools pull-right">
                                <a href="#" title="Depurar" class="glyphicon glyphicon-trash" data-toggle="modal" data-target="#PopUpDepurar"> </a>
                            </span>
                        </header>
                        <div class="panel-body">
                            <div class=" form">
                                <!-- CONTENIDO -->
                                
                                <table  style=' <% out.print(tblVisible); %>' class='table table-hover tabla_responsive'>
                                    <thead>
                                        <tr>
                                            <th>Código</th>
                                            <th>Fecha</th>
                                            <th>Usuario</th>
                                            <th>Servicio</th>
                                            <th>Estado</th>
                                            <th>Detalle</th>
                                        </tr>
                                    </thead>

                                    <% for (Object objeto : lstObjeto) {
                                            WS_Bit bitacora = (WS_Bit) objeto;
                                    %>
                                    <tr>
                                        <td><% out.print(utilidad.NuloToVacio(bitacora.getWsBitCod())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(bitacora.getWsBitFch())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(bitacora.getUsuario().getWsUsr())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(bitacora.getWsSrv())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(bitacora.getWsBitEst())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(bitacora.getWsBitDet())); %> </td>
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
        
        
        
        
        <!-- PopUp para depurar -->

        <div id="PopUpDepurar" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Depurar</h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <p>Confirma la eliminación de todos los registros?</p>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <form action="#" method="POST">
                        <button name="boton_confirmar" id="boton_confirmar" type="submit" value="DEPURAR" class="btn btn-danger" data-codigo="">Confirmar</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
                        
    </body>
</html>
