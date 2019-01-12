<%-- 
    Document   : pswRecovery
    Created on : 28-ago-2017, 15:02:54
    Author     : alvar
--%>

<%@page import="Enumerado.Constantes"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
<%

    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();

    String js_redirect = "window.location.replace('" + urlSistema + "');";
    
    String token = request.getParameter("tkn");

    String tknDecr = null;
    String perCod = null;
    String tkn = null;
    
    if(token != null)
    {
    
        tknDecr = Seguridad.GetInstancia().decrypt(token, 
                Constantes.ENCRYPT_VECTOR_INICIO.getValor(), 
                Constantes.ENCRYPT_SEMILLA.getValor());

        perCod = tknDecr.substring(0, tknDecr.indexOf(Constantes.SEPARADOR.getValor()));

        tkn = tknDecr.substring(tknDecr.indexOf(Constantes.SEPARADOR.getValor()), tknDecr.length());
        tkn = tkn.replace(Constantes.SEPARADOR.getValor(), "");
    
    }

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Recuperar contraseña</title>
        <jsp:include page="/masterPage/head.jsp"/>
    </head>
    <body class="body_clase">
        <div class="login_fondo">		
            <div class="login_contenedor">
                
                <div class="login_contenedorImg"><img src="Imagenes/ctc.png" /></div>
                <h1 class="login_titulo">RECUPERAR CONTRASEÑA</h1>
                <p class="login_texto">Bienvenido a Gestión, el servicio a estudiantes del Instituto CTC - Colonia. Indique su nueva contraseña</p>
                <form>
                    <div>
                        <input type="hidden" required="true" name="PerCod" id="PerCod" value='<%=perCod%>'>
                        <input type="hidden" required="true" name="tkn" id="tkn" value='<%=tkn%>'>
                    </div>
                    
                    <div class="login_form">
                        <input type="password" class="form-control login_inputBorde login_inputPass" required="true" name="pswNueva" id="pswNueva" placeholder="Contraseña nueva">
                        <input type="password" class="form-control login_inputPass" required="true" name="pswConfirmacion" id="pswConfirmacion" placeholder="Confirmar contraseña">
                    </div>

                    <button type="button" name="btnRecuperar" id="btnRecuperar" class="login_boton">RECUPERAR</button>
                </form>


            </div>
        </div>

        <div>
            <div id="div_pop_bkgr" name="div_pop_bkgr"></div>

            <div id="div_cargando" name="div_cargando">
                <div class="loading"></div>
            </div>

        </div>

        <div id="msgError" name="msgError" class="alert alert-success div_msg" style="display: none;"> 
            <label id="txtError" name="txtError">Error</label>
        </div>

        <script>
            $(document).ready(function () {
                MostrarCargando(false);

                var PerCod = $('#PerCod').val();
                if(PerCod == null)
                {
                    MostrarMensaje("ERROR", "Token incorrecto");
                }
                
                $('#btnRecuperar').click(function (event) {
                    MostrarCargando(true);
                    
                    var PerCod = $('#PerCod').val();
                    var tkn = $('#tkn').val();
                    var pswNueva = $('#pswNueva').val();
                    var pswConfirmacion = $('#pswConfirmacion').val();
                    
                    // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                    $.post('<% out.print(urlSistema); %>ABM_Persona', {
                        PerCod: PerCod,
                        tkn: tkn,
                        pswNueva: pswNueva,
                        pswConfirmacion: pswConfirmacion,
                        pAction: "PSW_RECOVERY"
                    }, function (responseText) {
                        var obj = JSON.parse(responseText);

                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                        if (obj.tipoMensaje != 'ERROR')
                        {
                            <%=js_redirect%>
                        } 

                    });
                    
                });

            });
        </script>
    </body>
</html>