<%-- 
    Document   : DefSincronizacionWW
    Created on : 18-ago-2017, 10:05:16
    Author     : alvar
--%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Entidad.BitacoraDetalle"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Entidad.BitacoraProceso"%>
<%@page import="Logica.LoBitacora"%>
<%@page import="Entidad.Sincronizacion"%>
<%@page import="Logica.LoSincronizacion"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
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
    
    Modo Mode = Modo.valueOf(request.getParameter("MODO"));
    String BitCod = request.getParameter("pBitCod");
    

    String js_redirect = "window.location.replace('" + urlSistema + "Administracion/BitacoraWW.jsp');";

    BitacoraProceso bit = new BitacoraProceso();

    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {
        Retorno_MsgObj retorno = (Retorno_MsgObj) LoBitacora.GetInstancia().obtener(Long.valueOf(BitCod));
        if (!retorno.SurgioError()) {
            bit = (BitacoraProceso) retorno.getObjeto();
        } else {
            out.print(retorno.getMensaje().toString());
        }
    }

    String CamposActivos    = "disabled";
    String nameButton       = "CONFIRMAR";
    String nameClass        = "btn-primary";

    switch (Mode) {
        case INSERT:
            CamposActivos = "enabled";
            break;
        case DELETE:
            nameButton    = "ELIMINAR";
            nameClass     = "btn-danger";
            break;
        case UPDATE:
            CamposActivos = "enabled";
            nameButton    = "MODIFICAR";
            break;
    }

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Bitácora</title>
        <jsp:include page="/masterPage/head.jsp"/>
        
        <script>
            $(document).ready(function () {
                $('#btn_guardar').click(function (event) {
                   
                            var BitCod= $('#BitCod').val();
                            var modo= $('#MODO').val();

                            if(modo == "DELETE")
                            {
                            $.post('<% out.print(urlSistema); %>ABM_Bitacora', {
                                pBitCod: BitCod,
                                pAction: modo
                            }, function (responseText) {
                                var obj = JSON.parse(responseText);
                                
                                MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                                if (obj.tipoMensaje != 'ERROR')
                                {
                                    <%=js_redirect%>
                                } 

                            });
                            }
                            else
                            {
                            <%=js_redirect%>
                            }
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
                        <!-- TABS -->
                        <header class="panel-heading">
                            <!-- TITULO -->
                            BITÁCORA
                            <!-- BOTONES -->
                            <span class="tools pull-right">
                                <a href="<% out.print(urlSistema); %>Administracion/BitacoraWW.jsp">Regresar</a>
                            </span>
                        </header>
			<div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <div name="datos_ocultos">
                                                        <input type="hidden" name="MODO" id="MODO" value="<% out.print(Mode); %>">
                                                        </div>
                                                        
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                            <div class="form-group "><label for="BitCod" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="BitCod" name="BitCod" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(bit.getBitCod())%>" ></div></div>
                                                            <div class="form-group "><label for="BitEst" class="control-label col-lg-3">Estado</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="BitEst" name="BitEst" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(bit.getBitEst())%>" ></div></div>
                                                            <div class="form-group "><label for="BitFch" class="control-label col-lg-3">Fecha</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="BitFch" name="BitFch" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(bit.getBitFch())%>" ></div></div>
                                                            <div class="form-group "><label for="BitMsg" class="control-label col-lg-3">Mensaje</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="BitMsg" name="BitMsg" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(bit.getBitMsg())%>" ></div></div>
                                                            <div class="form-group "><label for="BitProceso" class="control-label col-lg-3">Proceso</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="BitProceso" name="BitProceso" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(bit.getBitProceso())%>" ></div></div>
                                                            
                                                            
                                                            <div class="formulario_borde"></div>
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Detalle</h2>
                                                            </div>
                                                            
                                                            <table  class='table table-hover'>
                                                                <thead>
                                                                    <tr>
                                                                        <th>Código</th>
                                                                        <th>Estado</th>
                                                                        <th>Mensaje</th>
                                                                    </tr>
                                                                </thead>

                                                                <% for (BitacoraDetalle detalle : bit.getLstDetalle()) {
                                                                %>
                                                                <tr class="<%  out.print(RetornaClase(detalle.getBitDetEst())); %>">

                                                                    <td><% out.print( utilidad.NuloToVacio(detalle.getBitDetCod())); %> </td>
                                                                    <td><% out.print( utilidad.NuloToVacio(detalle.getBitDetEst())); %> </td>
                                                                    <td><% out.print( utilidad.NuloToVacio(detalle.getBitDetMsg())); %> </td>

                                                                </tr>
                                                                <%
                                                                    }
                                                                %>
                                                            </table>
                                                            
                                                            
                                                            <div class="form-group">
                                                                <div class="col-lg-offset-3 col-lg-6">
                                                                    <input name="btn_guardar" id="btn_guardar"  type="button"  class="btn <%=nameClass%>" data-accion="<%=Mode%>" value="<%=nameButton%>" />
                                                                    <input value="Cancelar" class="btn btn-default" type="button" onclick="<% out.print(js_redirect);%>"/>
                                                                </div>
                                                            </div>
                                                            
                                                        </form>
                                                    </div>
                                                </div>
                                            </section>
                                        </div>
                                    </div>
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

<%!
    private String RetornaClase(TipoMensaje tpoMsg) {
        switch (tpoMsg) {
            case ADVERTENCIA:
                return "alert alert-warning";
            case ERROR:
                return "alert alert-danger";
            case MENSAJE:
                return "alert alert-success";

        }
        return "";
    }

%>