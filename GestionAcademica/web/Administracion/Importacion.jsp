<%-- 
    Document   : DefSincronizacionWW
    Created on : 18-ago-2017, 10:05:16
    Author     : alvar
--%>
<%@page import="Enumerado.Constantes"%>
<%@page import="Entidad.Curso"%>
<%@page import="Logica.LoCurso"%>
<%@page import="Enumerado.TipoArchivo"%>
<%@page import="Entidad.PlanEstudio"%>
<%@page import="Entidad.Carrera"%>
<%@page import="Logica.LoCarrera"%>
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

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Importaciones</title>
        <jsp:include page="/masterPage/head.jsp"/>



        <script src="../JavaScript/JQueryFileUpload/js/vendor/jquery.ui.widget.js" type="text/javascript"></script>
        <script src="../JavaScript/JQueryFileUpload/js/jquery.iframe-transport.js" type="text/javascript"></script>
        <script src="../JavaScript/JQueryFileUpload/js/jquery.fileupload.js" type="text/javascript"></script>

        <link href="../JavaScript/JQueryFileUpload/css/jquery.fileupload.css" rel="stylesheet" type="text/css"/>

        <script>
            $(document).ready(function () {

                //-----------------------------------------------------------------------------
                //PLAN
                //-----------------------------------------------------------------------------
                $('#imp_alumno_plan').fileupload({
                    dataType: 'json',
                    maxFileSize: <%=Constantes.SIZE_FILE.getValor()%>,
                    maxNumberOfFiles: 1,
                    acceptFileTypes:  /(\.|\/)(xls|xlsx)$/i,
                    done: function (e, data) {
                        if (data != null)
                        {

                            var resultado = data.result;

                            if (resultado != null)
                            {

                                if (resultado.mensaje.tipoMensaje != "ERROR")
                                {
                                    var PlaEstCod = $('select[name=PlaEstCod]').val();
                                    var ArcCod = resultado.objeto;

                                    $.post('<%=urlSistema%>ABM_Importacion', {
                                        pPlaEstCod: PlaEstCod,
                                        pArcCod: ArcCod,
                                        pAction: "IMP_AL_PLAN"
                                    }, function (responseText) {
                                        var obj = JSON.parse(responseText);

                                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                                        if (obj.tipoMensaje != 'ERROR')
                                        {
                                            //QUE HAGO CUANDO TERMINE
                                        }

                                    });
                                } else
                                {
                                    MostrarMensaje(resultado.mensaje.tipoMensaje, resultado.mensaje.mensaje);
                                }
                            }
                        } else
                        {
                            alert('No se recibio response!');
                        }

                    },
                    progressall: function (e, data) {
                        var progress = parseInt(data.loaded / data.total * 100, 10);
                        $('#imp_alumno_plan_progress .progress-bar').css(
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

                //-----------------------------------------------------------------------------
                //CURSO
                //-----------------------------------------------------------------------------

                $('#imp_alumno_curso').fileupload({
                    dataType: 'json',
                    done: function (e, data) {
                        if (data != null)
                        {

                            var resultado = data.result;

                            if (resultado != null)
                            {

                                if (resultado.mensaje.tipoMensaje != "ERROR")
                                {
                                    var CurCod = $('select[name=CurCod]').val();
                                    var ArcCod = resultado.objeto;

                                    $.post('<%=urlSistema%>ABM_Importacion', {
                                        pCurCod: CurCod,
                                        pArcCod: ArcCod,
                                        pAction: "IMP_AL_CURSO"
                                    }, function (responseText) {
                                        var obj = JSON.parse(responseText);

                                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                                        if (obj.tipoMensaje != 'ERROR')
                                        {
                                            location.reload();
                                        }

                                    });
                                } else
                                {
                                    MostrarMensaje(resultado.mensaje.tipoMensaje, resultado.mensaje.mensaje);
                                }
                            }
                        } else
                        {
                            alert('No se recibio response!');
                        }

                    },
                    progressall: function (e, data) {
                        var progress = parseInt(data.loaded / data.total * 100, 10);
                        $('#imp_alumno_curso_progress .progress-bar').css(
                                'width',
                                progress + '%'
                                );
                    }
                });

                //-----------------------------------------------------------------------------
                //ESCOLARIDAD
                //-----------------------------------------------------------------------------

                $('#imp_alumno_escolaridad').fileupload({
                    dataType: 'json',
                    done: function (e, data) {
                        if (data != null)
                        {

                            var resultado = data.result;

                            if (resultado != null)
                            {

                                if (resultado.mensaje.tipoMensaje != "ERROR")
                                {
                                    var ArcCod = resultado.objeto;

                                    $.post('<%=urlSistema%>ABM_Importacion', {
                                        pArcCod: ArcCod,
                                        pAction: "IMP_AL_ESCOLARIDAD"
                                    }, function (responseText) {
                                        var obj = JSON.parse(responseText);

                                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                                        if (obj.tipoMensaje != 'ERROR')
                                        {
                                            location.reload();
                                        }

                                    });
                                } else
                                {
                                    MostrarMensaje(resultado.mensaje.tipoMensaje, resultado.mensaje.mensaje);
                                }
                            }
                        } else
                        {
                            alert('No se recibio response!');
                        }

                    },
                    progressall: function (e, data) {
                        var progress = parseInt(data.loaded / data.total * 100, 10);
                        $('#imp_alumno_escolaridad_progress .progress-bar').css(
                                'width',
                                progress + '%'
                                );
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
                            <!-- TITULO -->
                            IMPORTACIONES
                            <!-- BOTONES -->
                        </header>
                        <div class="panel-body">
                            <div class=" form">
                                <!-- CONTENIDO -->

                                <div class="formulario_borde"></div>
                                <div class="col-lg-offset-3 panel_contenedorTitulo">
                                    <h2>Alumnos e inscripciones a carreras</h2>
                                </div>

                                <div class="cmxform form-horizontal " >
                                    <div class="form-group ">
                                        <label for="PlaEstCod" class="control-label col-lg-3">Carrera - Plan</label>
                                        <div class="col-lg-6">
                                            <select class=" form-control inputs_generales" id="PlaEstCod" name="PlaEstCod" >
                                                <%
                                                    for (Object objeto : LoCarrera.GetInstancia().obtenerLista().getLstObjetos()) {
                                                        Carrera carrera = (Carrera) objeto;

                                                        for (PlanEstudio plan : carrera.getPlan()) {
                                                            out.println("<option  value='" + plan.getPlaEstCod() + "'>" + plan.getCarreraPlanNombre() + "</option>");
                                                        }
                                                    }
                                                %>
                                            </select>
                                        </div>
                                    </div>

                                    <!-- ARCHIVO -->
                                    <div class="row">
                                        <form id="imp_alumno_plan" action="<%=urlSistema%>UploadArchivo" method="POST" class="col-lg-offset-3 cmxform form-horizontal " enctype="multipart/form-data">

                                            <div class="col-lg-4">
                                                <input type="hidden" name="pTpoArc" value="<%=TipoArchivo.IMP_ALUMNO_CARRERA%>">

                                                <span class="btn btn-success fileinput-button">
                                                    <i class="glyphicon glyphicon-plus"></i>
                                                    <span>Seleccione un documento...</span>
                                                    <!-- The file input field used as target for the file upload widget -->
                                                    <input type="file" name="files[]" >
                                                </span>
                                            </div>
                                            <div id="imp_alumno_plan_progress" class="progress col-lg-4">
                                                    <div class="progress-bar progress-bar-success "></div>
                                            </div>
                                        </form>
  
                                    </div>

                                </div>


                                <div class="formulario_borde"></div>
                                <div class="col-lg-offset-3 panel_contenedorTitulo">
                                    <h2>Alumnos e inscripciones a cursos</h2>
                                </div>

                                <div class="cmxform form-horizontal " >
                                    <div class="form-group ">
                                        <label for="CurCod" class="control-label col-lg-3">Curso</label>
                                        <div class="col-lg-6">
                                            <select class=" form-control inputs_generales" id="CurCod" name="CurCod" >
                                                <%
                                                    for (Object objeto : LoCurso.GetInstancia().obtenerLista().getLstObjetos()) {
                                                        Curso curso = (Curso) objeto;

                                                        out.println("<option  value='" + curso.getCurCod() + "'>" + curso.getCurNom() + "</option>");
                                                    }
                                                %>
                                            </select>
                                        </div>
                                    </div>

                                    <!-- ARCHIVO -->
                                    
                                    <div class="row">

                                        <form id="imp_alumno_curso" action="<%=urlSistema%>UploadArchivo" method="POST" class="col-lg-offset-3 cmxform form-horizontal " enctype="multipart/form-data">

                                            <div class="col-lg-4">
                                                <input type="hidden" name="pTpoArc" value="<%=TipoArchivo.IMP_ALUMNO_CURSO%>">

                                                <span class="btn btn-success fileinput-button">
                                                    <i class="glyphicon glyphicon-plus"></i>
                                                    <span>Seleccione un documento...</span>
                                                    <!-- The file input field used as target for the file upload widget -->
                                                    <input type="file" name="files[]" >
                                                </span>
                                            </div>

                                            <div id="imp_alumno_curso_progress" class="progress col-lg-4">
                                                <div class="progress-bar progress-bar-success"></div>
                                            </div>
                                        </form>

                                        

                                    </div>
                                </div>

                                <div class="formulario_borde"></div>
                                <div class="col-lg-offset-3 panel_contenedorTitulo">
                                    <h2>Escolaridades</h2>
                                </div>


                                <!-- ARCHIVO -->
<div class="row">
                                <form id="imp_alumno_escolaridad" action="<%=urlSistema%>UploadArchivo" method="POST" class="col-lg-offset-3 cmxform form-horizontal " enctype="multipart/form-data">

                                    <div class="col-lg-4">
                                    <input type="hidden" name="pTpoArc" value="<%=TipoArchivo.IMP_ALUMNO_ESCOLARIDAD%>">

                                    <span class="btn btn-success fileinput-button">
                                        <i class="glyphicon glyphicon-plus"></i>
                                        <span>Seleccione un documento...</span>
                                        <!-- The file input field used as target for the file upload widget -->
                                        <input type="file" name="files[]" >
                                    </span>
                                    </div>

                                     <div id="imp_alumno_escolaridad_progress" class="progress col-lg-4">
                                    <div class="progress-bar progress-bar-success"></div>
                                </div>

                                </form>
                                    
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
