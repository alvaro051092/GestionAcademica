<%-- 
    Document   : uploadFoto
    Created on : 05-sep-2017, 22:59:04
    Author     : alvar
--%>

<%@page import="Enumerado.Constantes"%>
<%@page import="com.sun.tools.jxc.ap.Const"%>
<%@page import="Enumerado.TipoArchivo"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Entidad.Persona"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.LoPeriodo"%>
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

    Long PerCod     = (Long) session.getAttribute(NombreSesiones.USUARIO_PER.getValor());
    Persona persona = (Persona) LoPersona.GetInstancia().obtener(PerCod).getObjeto();
    
    String js_redirect = "location.replace('" + urlSistema + "');";
    

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Foto de perfil</title>
        <jsp:include page="/masterPage/head.jsp"/>
        <script src="JavaScript/JQueryFileUpload/js/vendor/jquery.ui.widget.js" type="text/javascript"></script>
        <script src="JavaScript/JQueryFileUpload/js/jquery.iframe-transport.js" type="text/javascript"></script>
        <script src="JavaScript/JQueryFileUpload/js/jquery.fileupload.js" type="text/javascript"></script>
        <script src="JavaScript/JQueryFileUpload/js/jquery.fileupload-process.js" type="text/javascript"></script>
        <script src="JavaScript/JQueryFileUpload/js/jquery.fileupload-validate.js" type="text/javascript"></script>
        
        <link href="JavaScript/JQueryFileUpload/css/jquery.fileupload.css" rel="stylesheet" type="text/css"/>
        
        <script>
            $(document).ready(function () {
                $('#fileupload').fileupload({
                    dataType: 'json',
                    maxFileSize: <%=Constantes.SIZE_FILE.getValor()%>,
                    maxNumberOfFiles: 1,
                    acceptFileTypes:  /(\.|\/)(gif|jpe?g|png)$/i,
                    done: function (e, data) {
                        if(data != null)
                        {
                            
                            var resultado = data.result;
                            
                            if(resultado != null)
                            {
                            
                                if(resultado.mensaje.tipoMensaje != "ERROR")
                                {   
                                    var PerCod= $('#PerCod').val();    
                                    var ArcCod= resultado.objeto;    

                                    $.post('<%=urlSistema%>ABM_Persona', {
                                        pPerCod: PerCod,
                                        pArcCod: ArcCod,
                                        pAction: "SUBIR_FOTO"
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
                                    MostrarMensaje(resultado.mensaje.tipoMensaje, resultado.mensaje.mensaje);
                                }
                            }
                        }
                        else
                        {
                            alert('No se recibio response!');
                        }

                    },
                    progressall: function (e, data) {
                        var progress = parseInt(data.loaded / data.total * 100, 10);
                        $('#progress .progress-bar').css(
                            'width',
                            progress + '%'
                        );
                    },
                    processfail:function(e, data){
                         if (data.files.error)
                         {
                             $.each(data.files, function (f, fileErr) {
                                MostrarMensaje("ERROR", fileErr.error);
                            });
                        }
                    },
                    messages : {
                        maxNumberOfFiles: 'Sólo se permite subir un archivo',
                        acceptFileTypes: 'Archivo invalido',
                        maxFileSize: 'El tamaño del archivo es superior a lo permitido',
                        minFileSize: 'El tamaño del archivo es inferior a lo permitido'
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
                        <header class="panel-heading">
                            FOTO DE PERFIL
                        </header>
                        <div class="panel-body">
                            <div class=" form"  style="text-align: center;">
                                <!-- METER LA FOTO ACA-->
                                <div name="foto_contenedor" style="width: ">
                                <%
                                    if(persona.getFotoBase64() == null)
                                    {
                                        out.println("<img class='foto_cambiar' src='" + urlSistema + "Imagenes/avatar.png'/>");
                                    }
                                    else
                                    {
                                        out.println("<img class='foto_cambiar' src='data:image/" + persona.getFotoExtension() + ";base64, " + persona.getFotoBase64() + "'/>");
                                    }

                                %>
                                </div>
                                
                                <!-- FORMULARIO -->
                                <input type="hidden" name="PerCod" id="PerCod" value="<%=PerCod%>">
                                <form id="fileupload" action="<%=urlSistema%>UploadArchivo" method="POST" enctype="multipart/form-data">
                                    <input type="hidden" name="pTpoArc" value="<%=TipoArchivo.FOTO_PERFIL%>">
                                    
                                    <span class="btn btn-success fileinput-button">
                                        <i class="glyphicon glyphicon-plus"></i>
                                        <span>Seleccione una foto...</span>
                                        <!-- The file input field used as target for the file upload widget -->
                                        <input type="file" name="files[]" >
                                    </span>
                                    
                                    <div style="width:185px; margin: 10px auto; ">
                                        <div id="progress" class="progress">
                                            <div class="progress-bar progress-bar-success"></div>
                                        </div>
                                    </div>
                                </form>

                                
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>

        <jsp:include page="/masterPage/footer.jsp"/>
    </body>
</html>
