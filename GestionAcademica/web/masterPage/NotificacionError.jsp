<%-- 
    Document   : NotificacionError
    Created on : 20-jul-2017, 15:04:40
    Author     : alvar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<noscript>
    <style type="text/css">
        .contenido *, .cabezal_contenedor * , #sidebar *, .login_fondo *,  .loading{
            display:none;
        }
    </style>
    
    <div class="noscriptmsg">
        <div id="msgError" name="msgError" class="alert alert-danger div_msg "> 
            <label id="txtError" name="txtError">Debe tener activado JavaScript para utilizar el sistema</label>
        </div>
    </div>
</noscript>

<div id="msgError" name="msgError" class="alert alert-success div_msg" style="display: none;"> 
    <label id="txtError" name="txtError">Error</label>
</div>

