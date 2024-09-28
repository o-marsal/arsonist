/**
 * Model is the main application model.
 * This object come from the backend, and should never me modified in the frontend.
 */
export interface Model {
    state: string;
    width: number;
    height: number;
    time: number;
    grid: string;
}

/**
 * Create a new empty model, used when waiting the real model from the backend.
 * @returns a new empty model.
 */
export function newModel() : Model {
    return {
        state: "STOPPED",
        width: 0,
        height: 0,
        time: 0,
        grid: ""
    };
}