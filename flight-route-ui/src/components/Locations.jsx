import axios from 'axios';
import {Button, Modal} from 'react-bootstrap';
import {useCallback, useEffect, useReducer, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {MdDelete, MdEdit} from 'react-icons/md';

const initialLocationState = {
    name: '',
    country: '',
    city: '',
    location_code: ''
};

const locationReducer = (state, action) => {
    switch (action.type) {
        case 'SET_FIELD':
            return {...state, [action.field]: action.value};
        case 'RESET':
            return {...initialLocationState};
        default:
            return state;
    }
};

function Locations() {
    const [data, setData] = useState([]);
    const [responseAlert, setResponseAlert] = useState("");
    const [newLocation, dispatch] = useReducer(locationReducer, initialLocationState);
    const [show, setShow] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [currentLocation, setCurrentLocation] = useState(null);

    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [locationToDelete, setLocationToDelete] = useState(null);

    const handleClose = () => setShow(false);
    const handleShow = (location = null) => {
        setIsEditing(location !== null);
        setCurrentLocation(location);
        dispatch({type: 'RESET'});
        if (location) {
            Object.keys(location).forEach(key => {
                dispatch({type: 'SET_FIELD', field: key, value: location[key]});
            });
        }
        setResponseAlert("");
        setShow(true);
    };

    const fetchLocations = useCallback(() => {
        axios.get(`/api/v1/locations`)
            .then((res) => {
                setData(res.data.data);
            })
            .catch((err) => {
                const errorMessage = err.response?.data?.message || 'An error occurred while fetching locations.';
                setResponseAlert(errorMessage);
            });
    }, []);

    useEffect(() => {
        fetchLocations();
    }, [fetchLocations]);

    function handleDeleteLocation(location) {
        setLocationToDelete(location);
        setShowDeleteModal(true);
    }

    function confirmDeleteLocation() {
        if (!locationToDelete) return;
        axios.delete(`/api/v1/locations/${locationToDelete.id}`)
            .then((res) => {
                setResponseAlert(res.data.message);
                fetchLocations();
            })
            .catch((err) => {
                const errorMessage = err.response?.data?.message || 'An error occurred while deleting the location.';
                setResponseAlert(errorMessage);
            })
            .finally(() => {
                setShowDeleteModal(false);
                setLocationToDelete(null);
            });
    }

    function addLocation() {
        axios.post(`/api/v1/locations`, newLocation, {
            headers: {'Content-Type': 'application/json'}
        })
            .then((res) => {
                handleClose();
                fetchLocations();
            })
            .catch((err) => {
                const errorMessage = err.response?.data?.message || 'An error occurred while adding the location.';
                setResponseAlert(errorMessage);
            });
    }

    function updateLocation() {
        if (!currentLocation) return;
        axios.put(`/api/v1/locations/${currentLocation.id}`, newLocation, {
            headers: {'Content-Type': 'application/json'}
        })
            .then((res) => {
                handleClose();
                fetchLocations();
            })
            .catch((err) => {
                const errorMessage = err.response?.data?.message || 'An error occurred while updating the location.';
                setResponseAlert(errorMessage);
            });
    }

    function handleInputChange(e) {
        const {name, value} = e.target;
        dispatch({type: 'SET_FIELD', field: name, value});
    }

    return (
        <>
            <div className="container p-5">
                <div className="mb-4">
                    <Button variant="dark" onClick={() => handleShow()}>
                        Add Location
                    </Button>
                </div>

                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>{isEditing ? 'Edit Location' : 'Add Location'}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {responseAlert && (
                            <div className="alert alert-warning alert-dismissible fade show" role="alert">
                                {responseAlert}
                                <button type="button" className="btn-close" data-bs-dismiss="alert"
                                        aria-label="Close"></button>
                            </div>
                        )}
                        <form>
                            <div className="mb-3">
                                <input
                                    type="text"
                                    className="form-control"
                                    id="name"
                                    placeholder="Location Name"
                                    name="name"
                                    value={newLocation.name}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="mb-3">
                                <input
                                    type="text"
                                    className="form-control"
                                    id="country"
                                    placeholder="Country"
                                    name="country"
                                    value={newLocation.country}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="mb-3">
                                <input
                                    type="text"
                                    className="form-control"
                                    id="city"
                                    placeholder="City"
                                    name="city"
                                    value={newLocation.city}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="mb-3">
                                <input
                                    type="text"
                                    className="form-control"
                                    id="location_code"
                                    placeholder="Location Code"
                                    name="location_code"
                                    value={newLocation.location_code}
                                    onChange={handleInputChange}
                                />
                            </div>
                        </form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="danger" onClick={handleClose}>
                            Close
                        </Button>
                        <Button variant="dark" onClick={isEditing ? updateLocation : addLocation}>
                            {isEditing ? 'Update' : 'Add'}
                        </Button>
                    </Modal.Footer>
                </Modal>

                <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                    <Modal.Header closeButton>
                        <Modal.Title>Confirm Delete</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        Are you sure you want to delete location
                        <br/>
                        <strong>ID:</strong> {locationToDelete?.id} <br/>
                        <strong>Name:</strong> {locationToDelete?.name} <br/>
                        <strong>Code:</strong> {locationToDelete?.location_code}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                            Cancel
                        </Button>
                        <Button variant="danger" onClick={confirmDeleteLocation}>
                            Delete
                        </Button>
                    </Modal.Footer>
                </Modal>

                {data.length > 0 ? (
                    <table className="table">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Name</th>
                            <th scope="col">City</th>
                            <th scope="col">Country</th>
                            <th scope="col">Location Code</th>
                            <th scope="col">Update</th>
                            <th scope="col">Delete</th>
                        </tr>
                        </thead>
                        <tbody>
                        {data.map((item) => (
                            <tr key={item.id}>
                                <th scope="row">{item.id}</th>
                                <td>{item.name}</td>
                                <td>{item.city}</td>
                                <td>{item.country}</td>
                                <td>{item.location_code}</td>
                                <td>
                                    <button onClick={() => handleShow(item)} type="button" className="btn btn-warning">
                                        <MdEdit/>
                                    </button>
                                </td>
                                <td>
                                    <button onClick={() => handleDeleteLocation(item)} type="button"
                                            className="btn btn-danger">
                                        <MdDelete/>
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <h5 className="text-center">You don't have a location. Create one now.</h5>
                )}
            </div>
        </>
    );
}

export default Locations;
