<%-- 
    Document   : DefVersion
    Created on : 25-jun-2017, 21:43:57
    Author     : alvar
--%>

<%@page import="Entidad.Version"%>
<%@page import="Logica.LoVersion"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    Version version = LoVersion.GetInstancia().obtener(Long.valueOf("1"));

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Versión</title>
        <jsp:include page="/masterPage/head.jsp"/>

        <script>
            $(document).ready(function () {
                
                $('#btn_guardar').click(function (event) {
                        if(validarDatos())
                        {
                            procesarDatos();
                        }
                });
                
                function validarDatos(){
                    
                    if(!$('#frm_general')[0].checkValidity())
                    {
                        var $myForm = $('#frm_general');
                        $myForm.find(':submit').click();
                        return false;
                    }

                    return true;
                }

            });
            
            function procesarDatos() {



                    var SisVerCod = $('#SisVerCod').val();
                    var SisVer = $('#SisVer').val();
                    var SisCrgDat = document.getElementById('SisCrgDat').checked;


                    if (SisVerCod == '' || SisVer == '')
                    {
                        MostrarMensaje("ERROR", "Completa los datos papa");
                    } else
                    {
                        // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                        $.post('<% out.print(urlSistema); %>AM_Version', {
                            pSisVerCod: SisVerCod,
                            pSisCrgDat: SisCrgDat,
                            pAction: "ACTUALIZAR"
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);

                            MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                        });
                    }
                }
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
                                VERSION
                            <!-- BOTONES -->
                            
                        </header>
        
                        <div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        
                                                        <div style="display:none" id="datos_ocultos" name="datos_ocultos">
                                                            <input type="hidden" id="SisVerCod" name="SisVerCod" placeholder="Código" disabled value="<% out.print(utilidad.NuloToVacio(version.getSisVerCod())); %>">
                                                        </div>
                                                        
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                                                                                    
                                                            <div class="form-group "><label for="SisVer" class="control-label col-lg-3">Versión</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="SisVer" name="SisVer" disabled value="<%=utilidad.NuloToVacio(version.getSisVer())%>" ></div></div>
                                                            <div class="form-group "><label for="SisCrgDat" class="control-label col-lg-3">Datos iniciales cargados</label><div class="col-lg-6"><input type="checkbox" id="SisCrgDat" name="SisCrgDat"  <%=utilidad.BooleanToChecked(version.getSisCrgDat())%> ></div></div>

                                                         
                                                            <div class="form-group">
                                                                <div class="col-lg-offset-3 col-lg-6">
                                                                    <input type="submit" style="display:none;">
                                                                    <input name="btn_guardar" id="btn_guardar"  type="button"  class="btn btn-primary" value="MODIFICAR" />
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
