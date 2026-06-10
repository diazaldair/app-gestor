const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// 1. CUANDO EL CLIENTE RESERVA -> NOTIFICAR AL DOCTOR
exports.notificarNuevaReserva = functions.database.ref('/workspaces/{clinicId}/appointments/{appointmentId}')
    .onCreate(async (snapshot, context) => {
        const cita = snapshot.val();
        const clinicId = context.params.clinicId;

        // Buscamos el token del médico en la ruta que ya usas en Kotlin
        const tokenSnap = await admin.database().ref(`/workspaces/${clinicId}/fcmToken`).once('value');
        const tokenDoctor = tokenSnap.val();

        if (!tokenDoctor) {
            console.log('Sin token para el Dr:', clinicId);
            return null;
        }

        const payload = {
            notification: {
                title: '¡Nueva Solicitud!',
                body: `Tienes una nueva cita para ${cita.serviceName}.`,
            },
            data: {
                type: 'NEW_BOOKING',
                id: cita.id
            }
        };

        return admin.messaging().sendToDevice(tokenDoctor, payload);
    });

// 2. CUANDO EL DR ACEPTA/RECHAZA -> NOTIFICAR AL CLIENTE
exports.notificarEstadoCita = functions.database.ref('/workspaces/{clinicId}/appointments/{appointmentId}/status')
    .onUpdate(async (change, context) => {
        const nuevoEstado = change.after.val();
        const appointmentId = context.params.appointmentId;
        const clinicId = context.params.clinicId;

        // Obtenemos patientId de la cita para saber a quién notificar
        const citaSnap = await admin.database().ref(`/workspaces/${clinicId}/appointments/${appointmentId}`).once('value');
        const cita = citaSnap.val();

        if (!cita || !cita.patientId) return null;

        const patientId = cita.patientId;

        // Buscamos el token del paciente
        const tokenSnap = await admin.database().ref(`/users/${patientId}/fcmToken`).once('value');
        const tokenPaciente = tokenSnap.val();

        if (!tokenPaciente) {
            console.log('Sin token para el paciente:', patientId);
            return null;
        }

        const titulo = nuevoEstado === 'ACCEPTED' ? '¡Cita Confirmada!' : 'Cita Rechazada';
        const mensaje = nuevoEstado === 'ACCEPTED' ?
            'Tu profesional ha aceptado la reserva.' : 'Lo sentimos, la cita no pudo ser aceptada.';

        const payload = {
            notification: {
                title: titulo,
                body: mensaje
            },
            data: {
                type: 'STATUS_UPDATE',
                status: nuevoEstado,
                appointmentId: appointmentId
            }
        };

        return admin.messaging().sendToDevice(tokenPaciente, payload);
    });
