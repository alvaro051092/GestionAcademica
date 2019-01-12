<%-- 
    Document   : DefPersona
    Created on : 30-jun-2017, 20:43:13
    Author     : alvar
--%>

<%@page import="Enumerado.Genero"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.Filial"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Entidad.Persona"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Enumerado.Modo"%>
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

    Persona persona = new Persona();

    if(usuario != null) persona = (Persona) loPersona.obtenerByMdlUsr(usuario).getObjeto();

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Persona</title>
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


            function procesarDatos(){
                    var PerCod= $('#PerCod').val();
                    var PerApe= $('#PerApe').val();
                    var PerDoc= $('#PerDoc').val();
                    var PerEml= $('#PerEml').val();
                    var PerNom= $('#PerNom').val();
                    var PerNotApp= $('#PerNotApp').is(':checked');
                    var PerNotEml= $('#PerNotEml').is(':checked');
                    var PerApe2= $('#PerApe2').val();
                    var PerCiudad= $('#PerCiudad').val();
                    var PerDir= $('#PerDir').val();
                    var PerDto= $('#PerDto').val();
                    var PerFchNac= $('#PerFchNac').val();
                    var PerGen= $('select[name=PerGen]').val();
                    var PerPais= $('#PerPais').val();
                    var PerTel= $('#PerTel').val();
                    
                    var modo = "<%=Modo.UPDATE%>";

                        // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                        $.post('<%=urlSistema%>ABM_Persona', {
                            pPerCod: PerCod,
                            pPerApe: PerApe,
                            pPerDoc: PerDoc,
                            pPerEml: PerEml,
                            pPerNom: PerNom,
                            pPerNotApp: PerNotApp,
                            pPerNotEml: PerNotEml,
                            pPerApe2: PerApe2,
                            pPerCiudad: PerCiudad,
                            pPerDir: PerDir,
                            pPerDto: PerDto,
                            pPerFchNac: PerFchNac,
                            pPerGen: PerGen,
                            pPerPais: PerPais,
                            pPerTel: PerTel,
                            pAction: modo
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);

                            MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                        });
                    
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
                                PERFIL
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

                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                            
                                                            <!-- DATOS PERSONALES -->
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2 class="">Datos Personales</h2>
                                                            </div>
                                                            
                                                            <input type="hidden" class=" form-control inputs_generales" id="PerCod" name="PerCod" disabled value="<%=utilidad.NuloToVacio(persona.getPerCod())%>" >

                                                            <div class="form-group "><label for="PerNom" class="control-label col-lg-3">Nombre</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="PerNom" name="PerNom"  value="<%=utilidad.NuloToVacio(persona.getPerNom())%>" ></div></div>
                                                            <div class="form-group "><label for="PerApe" class="control-label col-lg-3">Primer apellido</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="PerApe" name="PerApe"  value="<%=utilidad.NuloToVacio(persona.getPerApe())%>" ></div></div>
                                                            <div class="form-group "><label for="PerApe2" class="control-label col-lg-3">Segundo apellido</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerApe2" name="PerApe2"  value="<%=utilidad.NuloToVacio(persona.getPerApe2())%>" ></div></div>
                                                            <div class="form-group "><label for="PerDoc" class="control-label col-lg-3">Documento</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="PerDoc" name="PerDoc"  value="<%=utilidad.NuloToVacio(persona.getPerDoc())%>" ></div></div>
                                                            <div class="form-group "><label for="PerFchNac" class="control-label col-lg-3">Fecha de nacimiento</label><div class="col-lg-6"><input type="date" required class=" form-control inputs_generales" id="PerFchNac" name="PerFchNac"  value="<%=utilidad.NuloToVacio(persona.getPerFchNac())%>" ></div></div>
                                                            <div class="form-group "><label for="PerEml" class="control-label col-lg-3">Email</label><div class="col-lg-6"><input type="email" required class=" form-control inputs_generales" id="PerEml" name="PerEml"  value="<%=utilidad.NuloToVacio(persona.getPerEml())%>" ></div></div>
                                                            
                                                            <div class="form-group ">
                                                                <label for="PerGen" class="control-label col-lg-3">Género</label>
                                                                <div class="col-lg-6">
                                                                    <select class=" form-control inputs_generales" id="PerGen" name="PerGen"  >
                                                                    <%
                                                                        for (Genero genero : Genero.values()) {
                                                                            out.println("<option " + (genero == persona.getPerGen() ? "selected" : "") + " value='" + genero.name() + "'>" + genero.getValor() + "</option>");
                                                                        }
                                                                    %>
                                                                    </select>
                                                                </div>
                                                            </div>      
                                                            
                                                            <div class="form-group "><label for="PerTel" class="control-label col-lg-3">Teléfono</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerTel" name="PerTel"  value="<%=utilidad.NuloToVacio(persona.getPerTel())%>" ></div></div>
                                                            
                                                            <div class="formulario_borde"></div>
                                                            
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Dirección</h2>
                                                            </div>
                                                            
                                                            <!-- DIRECCION -->
                                                            <div class="form-group "><label for="PerDir" class="control-label col-lg-3">Dirección</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerDir" name="PerDir"  value="<%=utilidad.NuloToVacio(persona.getPerDir())%>" ></div></div>
                                                            <div class="form-group "><label for="PerCiudad" class="control-label col-lg-3">Ciudad</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerCiudad" name="PerCiudad"  value="<%=utilidad.NuloToVacio(persona.getPerCiudad())%>" ></div></div>
                                                            <div class="form-group "><label for="PerDto" class="control-label col-lg-3">Departamento</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerDto" name="PerDto"  value="<%=utilidad.NuloToVacio(persona.getPerDto())%>" ></div></div>
                                                            <div class="form-group "><label for="PerPais" class="control-label col-lg-3">Pais</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerPais" name="PerPais"  value="<%=utilidad.NuloToVacio(persona.getPerPais())%>" ></div></div>

                                                            <div class="formulario_borde"></div>
                                                            
                                                            <!-- NOTIFICACION -->
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Notificación</h2>
                                                            </div>
                                                            
                                                            <div class="form-group "><label for="PerNotApp" class="control-label col-lg-3">Aplicación</label><div class="col-lg-6"><input type="checkbox" class=" inputs_generales" id="PerNotApp" name="PerNotApp"  <%=utilidad.BooleanToChecked(persona.getPerNotApp())%> ></div></div>
                                                            <div class="form-group "><label for="PerNotEml" class="control-label col-lg-3">Email</label><div class="col-lg-6"><input type="checkbox" class=" inputs_generales" id="PerNotEml" name="PerNotEml"  <%=utilidad.BooleanToChecked(persona.getPerNotEml())%> ></div></div>
                                                            
                                                            <div class="form-group">
                                                                <div class="col-lg-offset-3 col-lg-6">
                                                                    <input type="submit" style="display:none;">
                                                                    <input type="button" class="btn btn-primary" data-accion="<%=Modo.UPDATE%>" name="btn_guardar" id="btn_guardar" value="MODIFICAR">
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
