package com.gestorplus.appgestor.edit_services.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.edit_services.presentation.state.EditServiceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditServiceScreen(
    onBack: () -> Unit
) {
    // Simulamos el estado por ahora
    var name by remember { mutableStateOf("Consulta Médica General") }
    var category by remember { mutableStateOf("Medicina General") }
    var description by remember { mutableStateOf("Consulta integral de medicina general para diagnóstico y tratamiento inicial.") }
    var price by remember { mutableStateOf("50.00") }

    Scaffold(
        containerColor = Color(0xFF0F172A),
        topBar = {
            TopAppBar(
                title = { Text("Editar Servicio", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Placeholder para subir imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFF1E293B).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .clickable { /* TODO */ },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AddAPhoto, null, tint = Color.Gray, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("Subir imagen de portada", color = Color.Gray, fontSize = 14.sp)
                }
            }

            EditField(label = "NOMBRE DEL SERVICIO", value = name, onValueChange = { name = it })
            
            Column {
                Text("CATEGORÍA", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.Gray) },
                    shape = RoundedCornerShape(12.dp),
                    colors = editFieldColors()
                )
            }

            EditField(label = "DESCRIPCIÓN", value = description, onValueChange = { description = it }, minLines = 3)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                EditField(
                    label = "PRECIO", 
                    value = price, 
                    onValueChange = { price = it }, 
                    modifier = Modifier.weight(1f),
                    trailingIcon = { Text("$", color = Color.Gray) }
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text("MONEDA", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "USD",
                        onValueChange = { },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = { Icon(Icons.Default.Lock, null, tint = Color.Gray, modifier = Modifier.size(16.dp)) },
                        shape = RoundedCornerShape(12.dp),
                        colors = editFieldColors()
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { /* Save logic */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) {
                Text("Guardar Cambios", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Text(label, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            minLines = minLines,
            shape = RoundedCornerShape(12.dp),
            colors = editFieldColors(),
            trailingIcon = trailingIcon
        )
    }
}

@Composable
fun editFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = Color(0xFF1E293B),
    focusedContainerColor = Color(0xFF1E293B),
    unfocusedBorderColor = Color.Transparent,
    focusedBorderColor = Color(0xFF3B82F6),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White
)
